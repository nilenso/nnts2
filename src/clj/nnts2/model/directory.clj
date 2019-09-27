(ns nnts2.model.directory
  (:require [nnts2.db.directory :as db]
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
  (let [to-insert (filter #(= parent-id (:parent-id %)) remaining-rows)
        next-iter-rows (clojure.set/difference (set remaining-rows) (set to-insert))]
    (reduce
     (fn [acc v]
       (assoc
        acc
        (:id v)
        (directory-rows->nested-directories (dissoc v :id :parent-id) (:id v) next-iter-rows)))
     tree
     to-insert)))


(defn list [params]
  "if recursive is true in params, then full directory sub tree will be returned,
   if false, then only immediate children are returned"
  (let [recursive (:recursive params)
        parent-id (:parent-id params)
        filter-by (if recursive (dissoc params :parent-id :recursive) (dissoc params :recursive))
        directory-rows (db/get filter-by)]
    (directory-rows->nested-directories {} parent-id directory-rows)))


(defn get-one-item [params]
  (let [directory (first (db/get params))]
    (assoc directory :directories (list (clojure.set/rename-keys params {:id :parent-id})))))


(defn create [params]
  (res/response (db/create params)))
