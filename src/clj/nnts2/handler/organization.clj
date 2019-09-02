(ns nnts2.handler.organization
  (:require [nnts2.db.organization :as db]
            [nnts2.spec-helpers :as spec-helper]
            [nnts2.model.organization-spec :as spec]
            [ring.util.response :as res]))

(defn create [{:keys [body] :as request}]
  (if (spec/valid? body)
    (let [org (db/insert body)]
      (if (nil? org)
        (->  (res/response "Slug exists")
             (res/status 409))
        (res/response org)))
    (spec-helper/invalid (spec/explain-str body))))
