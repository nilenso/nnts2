(ns nnts2.user.middleware
  (:require [clj-http.client :refer [with-middleware get default-middleware]]
            [cheshire.core :as json]
            [nnts2.middleware :refer [snake->kebab]]))


(defn oauth2-user-info [handler]
  (fn [request]
    (let [access-token (get-in request [:oauth2/access-tokens :google :token])
          user-info (-> (get "https://www.googleapis.com/oauth2/v3/userinfo"
                             {:headers {"Authorization" (str "Bearer " access-token)}} {:as :json})
                        (:body)
                        (json/parse-string true)
                        (snake->kebab))]
      (handler (assoc-in request [:session :user-info] user-info)))))
