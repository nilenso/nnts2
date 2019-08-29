(ns nnts2.user.middleware
  (:require [nnts2.user.db :as db]))


(defn wrap-nnts-user-id [handler]
  (fn [request]
    (let [email (get-in request [:google-user :email])
          user (db/get-by-email email)]
      (handler (assoc-in request [:nnts-user] (:id user))))))
