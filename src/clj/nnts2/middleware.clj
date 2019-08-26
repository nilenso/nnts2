(ns nnts2.middleware
  (:require [clojure.walk :as walk]
            [clojure.string :as str]
            [compojure.response :as response]
            [clojure.stacktrace :as st]
            [clojure.tools.logging :as log])
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
      (log/debug {:event    ::request-response
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