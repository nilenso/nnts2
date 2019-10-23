(ns nnts2.model.organization
  (:require [nnts2.db.organization :as db]))

(defn get-membership-orgs [member-data]
  (db/get-membership member-data))

(defn get-org [org-params]
  (db/get org-params))

(defn add-member [member-data]
  (db/add-user member-data))

(defn is-member-of-org [user-id org-id]
  "check if a user is part of an organization"
  (> (count (get-membership-orgs
             {:org-id  org-id
              :user-id user-id})) 0))

(defn is-admin-of-org [user-id org-id]
  "check if a user is part of an organization"
  (> (count (get-membership-orgs
             {:org-id  org-id
              :member  "admin"
              :user-id user-id})) 0))

(defn create-org [{:keys [slug] :as org-data}]
  (let [does-slug-exist (-> {:slug slug}
                            db/get
                            first)]
    (if does-slug-exist
      {:error :slug-already-exists}
      (db/create org-data))))

(defn find-or-create-org [org-data]
  (let [existing-org (first (get-org org-data))]
    (if existing-org
      {:error :org-same-name-slug-exists}
      (create-org org-data))))

(defn create-org-with-member [org-data user-id]
  (let [org (find-or-create-org org-data)]
    (when-not (:error org)
      (add-member {:org-id  (:id org)
                   :role    "admin"
                   :user-id user-id}))
    org))
