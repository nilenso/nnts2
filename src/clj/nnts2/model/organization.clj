(ns nnts2.model.organization
  (:require [nnts2.db.organization :as db]))

(defn org-exists [org-id nnts-user]
  "check if organization exists and if user is member"
  (> (count (db/get-membership
             {:org-id  org-id
              :user-id nnts-user})) 0))

(defn create-org [org-data]
  (db/create org-data))

(defn add-member [member-data]
  (db/add-user member-data))

(defn get-org [org-params]
  (db/get org-params))

(defn create-org-add-membership [org-data user]
  (let [existing-org (first (get-org org-data))
        member-data  {:user-id user
                      :role    "admin"
                      :org-id  (:id existing-org)}]
    (if existing-org
      (do
        (add-member member-data)
        existing-org)
      (let [new-org (db/create org-data)]
        (do
          (add-member (assoc member-data :org-id (:id new-org)))
          new-org)))))
