(ns nnts2.handler.user
  (:require [ring.util.response :as res]
            [clj-http.client :refer [get]]
            [nnts2.model.user :as model]
            [nnts2.spec.user :as spec]
            [nnts2.spec-helpers :as spec-helper]))

(defn create [{:keys [google-user]}]
  (if (spec/valid? google-user)
    (do (model/create google-user)
        (res/redirect (str "/home")))
    (spec-helper/invalid (spec/explain-str google-user))))
