(ns nnts2.user.middleware
  (:require [clj-http.client :refer [with-middleware get default-middleware]]
            [cheshire.core :as json]
            [nnts2.middleware :refer [hyphenize-collection]]
            [nnts2.user.db :as db]))

(defn wrap-nnts-user-id [handler]
  (fn [request]
    (let [email (get-in request [:google-user :email])
          user (db/get-by-email email)]
      (handler (assoc request :nnts-user (:id user))))))
