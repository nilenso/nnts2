(ns nnts2.user.handler
  (:require [ring.util.response :as res]
            [clj-http.client :refer [with-middleware get default-middleware]]
            [nnts2.user.db :as db]
            [ring.util.response]
            [nnts2.user.spec :as spec]
            [nnts2.spec-helpers :as spec-helper]))


(defn create [{:keys [google-user]}]
  (if (spec/valid? google-user)
    (do (db/create google-user)
        (-> (res/redirect (str "/home"))))
    (spec-helper/invalid (spec/explain-str google-user))))


(defn info [id]
  (res/response (db/get-by-id (Integer/parseInt id))))
