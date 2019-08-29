(ns nnts2.member.handler
  (:require [ring.util.response :as res]
            [nnts2.member.db :as db]))

(defn add [{:keys [params]}]
  (let [member {:user-id (Integer/parseInt (:user-id params)) :org-id (Integer/parseInt (:org-id params))}]
    (if (spec/valid? member)
      (let [member (db/add params)]
        (if (nil? member)
          (-> (res/response "User is already a member of this organization")
              (res/status 409))
          (res/response member)))
      (spec-helper/invalid (spec/explain-str body)))))

(defn get-orgs [{:keys [params]}]
  (let [orgs (db/get-orgs (:id params))]
    (->  (res/response)
         (assoc :body orgs))))
