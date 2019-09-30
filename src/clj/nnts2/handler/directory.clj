(ns nnts2.handler.directory
  (:require
   [nnts2.model.directory :as directory]
   [nnts2.model.directory-spec :as spec]
   [ring.util.response :as res]))


(defn create [request org-id body]
  "create a directory for an org, with/without a parent directory"
                                        ; should have org-id and dir-id (nullable) in params, org-id in route params and dir-id in params
  (let [nnts-user (:nnts-user request)
        dir-details (-> body
                        (assoc :org-id org-id)
                        (assoc :created-by-id nnts-user))]
    (res/response (if (spec/valid? dir-details)
                    (directory/create dir-details)
                    (spec/explain-str? dir-details)))))



(defn list [request org-id parent-id recursive]
  "get directories based on org param"
  (let [params  {:org-id org-id :parent-id parent-id :recursive recursive}
        nnts-user (:nnts-user request)]
    (res/response (directory/list params))))

(defn find [request org-id id]
  (let [params {:org-id org-id :id id}]
    (res/response (directory/get-one-item params))))
