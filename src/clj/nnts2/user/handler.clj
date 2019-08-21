(ns nnts2.user.handler
  (:require [ring.util.response :as res]
            [clj-http.client :refer [with-middleware get default-middleware]]
            [nnts2.user.db :as db]
            [ring.util.response]
            [nnts2.user.spec :as spec]
            [nnts2.spec-helpers :as spec-helper]))

(defn add [{{:keys [user-info]} :session}]
  (if (spec/valid? user-info)
    (do (db/add user-info)
        (str user-info))
    (spec-helper/invalid (spec/explain-str user-info))))
