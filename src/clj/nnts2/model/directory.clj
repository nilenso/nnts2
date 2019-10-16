(ns nnts2.model.directory
  (:require [nnts2.db.directory :as db]
            [nnts2.model.organization :as org]
            [compojure.coercions :refer [as-uuid]]
            [ring.util.response :as res]))

(defn directory-rows->nested-directories
  "converting from any rows with id-parent relationship to a nested map
  tree initial value {}
  parent-id nil (unless you want to get a subtree of the parent id given)
  remaining-rows all input rows"
  [tree
   parent-id
   remaining-rows]
  (let [to-insert      (filter #(= parent-id (:parent-id %)) remaining-rows)
        next-iter-rows (clojure.set/difference (set remaining-rows) (set to-insert))]
    (reduce
     (fn [acc {:keys [id name org-id]}]
       (conj
        acc
        {:id          id
         :name        name
         :org-id      org-id
         :directories (directory-rows->nested-directories [] id next-iter-rows)}))
     tree
     to-insert)))

(defn list [{:keys [show-tree parent-id] :as params}]
  "if show-tree is true in params, then full directory sub tree will be returned
   if false then only immediate children are returned"
  (let [filter-by      (if show-tree (dissoc params :parent-id :show-tree) (dissoc params :show-tree))
        directory-rows (db/get filter-by)]
    (directory-rows->nested-directories [] parent-id directory-rows)))

(defn get-one-item [params]
  (let [directory (first (db/get params))]
    (assoc directory :directories (list (clojure.set/rename-keys params {:id :parent-id})))))

(defn dir-exists [params]
  (> (count (db/get (select-keys params [:name :parent-id :org-id]))) 0))

(defn create [params]
  "create directory if org exists and orgs match if there is a parent directory mentioned"
  (if (org/org-exists (:org-id params) (:created-by-id params))
    (if-not (dir-exists params)
      (if-not (:parent-id params)
        (db/create params)
        (if (= (:org-id params) (:org-id (get-one-item {:id (:parent-id params)})))
          (db/create params)
          {:error :organizations-dont-match}))
      {:error :dir-already-exists})
    {:error :org-doesnt-exist}))
