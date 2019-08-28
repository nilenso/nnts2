(ns nnts2.user.middleware
  (:require [nnts2.user.db :as db]))


(defn wrap-nnts-user-id [handler]
  (fn [request]
    (let [email (get-in request [:session :user-info :email])
          user (db/get-by-email email)]
      (handler (assoc-in request [:session :user-info :nnts-id] (:id user))))))
