(ns nnts2.user.handler
  (:require [ring.util.response :as res]
            [clj-http.client :refer [with-middleware get default-middleware]]
            [nnts2.user.db :as db]
            [ring.util.response]
            [nnts2.user.spec :as spec]
            [nnts2.spec-helpers :as spec-helper]))

(defn create [{{:keys [user-info]} :session}]
  (if (spec/valid? user-info)
    (db/create user-info)
    (spec-helper/invalid (spec/explain-str user-info))))
