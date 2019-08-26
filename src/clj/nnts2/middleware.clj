(ns nnts2.middleware
  (:require [clojure.walk :as walk]
            [clojure.string :as str]
            [compojure.response :as response]
            [clojure.stacktrace :as st]
            [clj-http.client :as clj-http]
            [clojure.tools.logging :as log]
            [cheshire.core :as json]
            [ring.util.response :as res])
  (:import (java.io InputStream)))

(defn snake->kebab
  [word]
  (-> word keyword str (str/replace "_" "-") (subs 1) keyword))

(defn hyphenize-collection
  [data]
  (let [transform-map (fn [form]
                        (if (map? form)
                          (reduce-kv #(assoc %1 (snake->kebab %2) %3) {} form)
                          form))]
    (walk/postwalk transform-map data)))

(defn wrap-kebab-case
  [handler]
  (fn [request]
    (let [kebab-request (select-keys request [:session :params :body])]
      (-> request
          (dissoc [:session :params :body])
          (conj (hyphenize-collection kebab-request))
          (handler)))))

(defn wrap-log-request-response
  [handler]
  (fn [request]
    (let [response (handler request)]
      (prn {:event    ::request-response
            :request  request
            :response (if (instance? InputStream (:body response))
                        (assoc response :body-type :input-stream)
                        response)})
      response)))

(defn wrap-exception-handling
  "Catches an exception and returns the appropriate HTTP response."
  [handler-fn]
  (fn [request]
    (try (handler-fn request)
         (catch Exception e
           (log/error "Uncaught exception: " {:request    (prn-str request)
                                              :stacktrace (with-out-str
                                                            (st/print-stack-trace e))})
           {:status 500
            :body   "Internal Server error"}))))

(defn not-found
  [body]
  (fn handler
    ([request]
     (-> (response/render body request)
         (ring.util.response/status 200)
         (cond-> (= (:request-method request) :head) (assoc :body nil))))
    ([request respond raise]
     (respond (handler request)))))

(defn debug [x] (prn x) x)

(defn wrap-validate-access-token [handler]
  (fn [request]
    (let [access-token (get-in request [:session :ring.middleware.oauth2/access-tokens :google :token])]
      (try (let [response (clj-http/get "https://www.googleapis.com/oauth2/v3/userinfo"
                                        {:headers {"Authorization" (str "Bearer " access-token)}} {:as :json :throw-exceptions false})
                 user-info (-> response
                               (:body)
                               (json/parse-string true)
                               (hyphenize-collection))]
             (handler (assoc-in request [:session :user-info] user-info)))
           (catch Exception e
             (-> (res/response "Not authorized")
                 (res/status 401)))))))
