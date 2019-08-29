(ns nnts2.user.handler
  (:require [ring.util.response :as res]
            [clj-http.client :refer [with-middleware get default-middleware]]
            [nnts2.user.db :as db]
            [ring.util.response]
            [nnts2.user.spec :as spec]
            [nnts2.spec-helpers :as spec-helper]
            [clojure.string :as str]))

(defn create [{{:keys [user-info]} :session}]
  (if (spec/valid? user-info)
    (let [user (db/create user-info)]
      (-> (res/redirect (str "/home"))
          (assoc :body user)))
    (spec-helper/invalid (spec/explain-str user-info))))

(defn info [id]
  (res/response (db/get-by-id (Integer/parseInt id))))
