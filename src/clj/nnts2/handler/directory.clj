(ns nnts2.handler.directory
  (:require [nnts2.model.organization :as org]
            [nnts2.model.directory :as directory]
            [nnts2.model.directory-spec :as spec]
            [ring.util.response :as res]))


(defn create [request]
  "create a directory for an org, with/without a parent directory"
                                        ; should have org-id and dir-id (nullable) in params, org-id in route params and dir-id in params
  (let [org-id (java.util.UUID/fromString
                (get-in request [:params :org-id]))
        nnts-user (:nnts-user request)
        dir-details (conj (:body request)
                          {:org-id org-id :created-by-id nnts-user})]
    (prn dir-details)
    (if (org/org-exists org-id (:nnts-user request))
      (if (spec/valid? dir-details)
        (directory/create dir-details)
        (spec/explain-str? dir-details))
      (str "org doesnt exist"))))



(defn get-list [request]
  "get directories based on org param"
                                        ;first check if org exists -- we will worry about permissions later
                                        ;second - pass org id
  (let [params (dissoc (:params request) :*)
        nnts-user (:nnts-user request)
        org-id (java.util.UUID/fromString (:org-id params))]
    (prn params)
    (if (org/org-exists org-id nnts-user)
      (res/response (directory/get-list params))
      (str "org doest exiost"))))

(defn get-one [request] (str request))
