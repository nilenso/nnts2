(ns nnts2.model.organization
  (:require [nnts2.db.organization :as db]))

(defn org-exists [org-id nnts-user]
  "check if organization exists and if user is member"
  (> (count (db/get
             {:org-id org-id
              :nnts-user nnts-user})) 0))
