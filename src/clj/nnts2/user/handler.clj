(ns nnts2.user.handler
  (:require [ring.util.response :as res]
            [clj-http.client :as client]))

(defn add [request]
  (let [access-token (get-in request [:oauth2/access-tokens :google :token])
        user-info (:body (client/get "https://www.googleapis.com/oauth2/v3/userinfo"
                                     {:headers {"Authorization" (str "Bearer " access-token)}}))]
    user-info))