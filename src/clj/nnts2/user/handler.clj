(ns nnts2.user.handler
  (:require [ring.util.response :as res]
            [clj-http.client :refer [with-middleware get default-middleware]]
            [nnts2.user.db :as db]
            [cheshire.core :as json]
            [nnts2.middleware :refer [wrap-kebab-case]]))

(defn add [request]
  (let [access-token (get-in request [:oauth2/access-tokens :google :token])
        user-info (-> (get "https://www.googleapis.com/oauth2/v3/userinfo"
                           {:headers {"Authorization" (str "Bearer " access-token)}} {:as :json})
                      (:body)
                      (json/parse-string true))]
    (db/add user-info)))