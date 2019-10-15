(ns nnts2.handler.user
  (:require [ring.util.response :as res]
            [clj-http.client :refer [get]]
            [nnts2.db.user :as user-db]
            [nnts2.spec.user :as user-spec]
            [nnts2.spec-helpers :as spec-helper]))

(defn create [{:keys [google-user]}]
  (if (user-spec/valid? google-user)
    (do (user-db/create google-user)
        (res/redirect (str "/home")))
    (spec-helper/invalid (user-spec/explain-str google-user))))
