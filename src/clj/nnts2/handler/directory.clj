(ns nnts2.handler.directory
  (:require   [nnts2.model.directory :as directory]
              [ring.util.response :as res]))

(defn create [request org-id body]
  "create a directory for an org, with/without a parent directory"
  (let [nnts-user   (:nnts-user request)
        dir-details (-> body
                        (assoc :org-id org-id)
                        (assoc :created-by-id nnts-user))
        dir         (directory/create dir-details)]
    (condp #(contains? %2 %1) dir
      :error (-> (res/response (str (:error dir)))
                 (res/status 409))
      :id    (-> (res/response dir)
                 (res/status 200)))))

(defn list [request org-id parent-id show-tree]
  "get directories based on org param"
  (let [params {:org-id org-id :parent-id parent-id :show-tree show-tree}]
    (res/response (directory/list params))))

(defn find [request org-id id]
  "get full details of a single directory with id"
  (let [params {:org-id org-id :id id}]
    (res/response (directory/get-one-item params))))
