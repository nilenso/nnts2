(ns nnts2.model.organization
  (:require [nnts2.db.organization :as db]))

(defn org-exists [org-id nnts-user]
  "check if organization exists and if user is member"
  (prn "checking if org exists")
  (> (count (db/get
             {:org-id (java.util.UUID/fromString org-id)
              :nnts-user nnts-user})) 0))
