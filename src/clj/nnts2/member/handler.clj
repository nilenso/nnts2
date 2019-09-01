(ns nnts2.member.handler
  (:require [ring.util.response :as res]
            [nnts2.member.db :as db]
            [nnts2.member.spec :as spec]
            [nnts2.spec-helpers :as spec-helper]
            [clojure.string :as str]))

(defn add [{:keys [params]}]
  (let [params {:user-id (java.util.UUID/fromString (:user-id params))
                :org-id (java.util.UUID/fromString (:org-id params))
                :role (:role params)}]
    (if (spec/valid? params)
      (let [member (db/add params)]
        (if (nil? member)
          (-> (res/response "User is already a member of this organization")
              (res/status 409))
          (res/response member)))
      (spec-helper/invalid (spec/explain-str params)))))

(defn get-orgs [{:keys [params]}]
  (let [orgs (db/get-orgs (:id params))]
    (->  (res/response)
         (assoc :body orgs))))
