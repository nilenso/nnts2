(ns nnts2.model.user
  (:require [nnts2.db.user :as user-db]
            [nnts2.model.organization :as org-model]
            [nnts2.model.directory :as dir-model]))

(defn create [user-data]
  "create user"
  (let [{:keys [id] :as user} (user-db/create user-data)
        org-data              {:name "MyNotes" :slug (str "MyNotes-" id)}
        org                   (org-model/create-org-with-member org-data id)
        dir-data              {:name "general" :org-id (:id org) :parent-id nil :created-by-id id}
        dir                   (dir-model/create dir-data)]
    user-data))
