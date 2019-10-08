(ns nnts2.http-middleware
  (:require [clojure.string :as str]
            [clojure.stacktrace :as st]
            [clojure.tools.logging :as log]
            [compojure.response :as response]
            [clj-http.client :as clj-http]
            [cheshire.core :as json]
            [ring.util.response :as res]
            [nnts2.db.user :as user-db]
            [nnts2.utils :as utils])
  (:import (java.io InputStream StringReader)))

(defn wrap-kebab-case
  [handler]
  (fn [request]
    (let [kebab-request (select-keys request [:session :params :body])]
      (-> request
          (dissoc [:session :params :body])
          (conj (utils/hyphenize-collection kebab-request))
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

(defn wrap-validate-access-token [handler]
  (fn [request]
    (let [access-token (or (get-in request [:session :ring.middleware.oauth2/access-tokens :google :token])
                           (get-in request [:headers "authorization"]))]

      (let [response (clj-http/get "https://www.googleapis.com/oauth2/v3/userinfo"
                                   {:throw-exceptions false
                                    :headers          {"Authorization" (if (not (str/starts-with? access-token "Bearer "))
                                                                         (str "Bearer " access-token)
                                                                         access-token)}
                                    :as               :json})]
        (if (< (:status response) 400)
          (handler (assoc-in request [:google-user] (-> response
                                                        (:body)
                                                        (utils/hyphenize-collection))))
          (-> (res/response "Not authorized")
              (res/status 401)))))))

(defn wrap-nnts-user-id [handler]
  (fn [request]
    (let [email (get-in request [:google-user :email])
          user (user-db/get-by-email email)]
      (handler (assoc-in request [:nnts-user] (:id user))))))
