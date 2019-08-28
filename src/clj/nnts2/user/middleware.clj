(ns nnts2.user.middleware
  (:require ;[clj-http.client :refer [with-middleware get default-middleware]]
  ;          [cheshire.core :as json]
   ;         [nnts2.middleware :refer [hyphenize-collection]]
            [nnts2.user.db :as db]))


#_(defn oauth2-user-info [handler]
  (fn [request]
    (let [access-token (get-in request [:oauth2/access-tokens :google :token])
          user-info (-> (get "https://www.googleapis.com/oauth2/v3/userinfo"
                             {:headers {"Authorization" (str "Bearer " access-token)}} {:as :json})
                        (:body)
                        (json/parse-strring true)
                        (hyphenize-collection))]
      (handler (assoc-in request [:session :user-info] user-info)))))



(defn wrap-nnts-user-id [handler]
  (fn [request]
    (let [email (get-in request [:session :user-info :email])
          user (db/get-by-email email)]
      (handler (assoc-in request [:session :user-info :nnts-id] (:id user))))))
