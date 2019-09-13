(ns nnts2.handler.directory
  (:require [nnts2.model.organization :as org]
            [nnts2.model.directory :as directory]))

(defn get [request]
  "get directories based on org param"
  ;first check if org exists -- we will worry about permissions later
                                        ;second - pass org id
  (let [org-id (get-in request [:params :org-id])]
    (if (org/org-exists org-id (:nnts-user request))
      (directory/get {:org-id org-id})
      (str "org doest exiost"))))


;create fn -- check if org exists, call spec
