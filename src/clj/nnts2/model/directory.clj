(ns nnts2.model.directory
  (:require [nnts2.db.directory :as db]
            [compojure.coercions :refer [as-uuid]]
            [ring.util.response :as res]))


;;also do all string to uuid conversion here
(defn to-uuid- [val]
  (cond
    (uuid? val) val  ;;should remove this co
    :else (as-uuid val)))


(defn keyset-to-uuid [map [k & more]]
  (if k
    (if (get map k)
      (recur (assoc map k (to-uuid- (get map k))) more)
      (recur map more))
    map))


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

#_(defn
    ([rows-in] (directory-rows->nested-directories rows-in nil))
    ([rows-in parent-directory-id] (flat-rows-to-nested-map {} parent-directory-id rows-in)))



(defn get-list [params]
  "if recursive is true in params, then full directory sub tree will be returned,
   if false, then only immediate children are returned"
  (let [recursive (:recursive params)
        in-params (if recursive (dissoc params :parent-id :recursive) (dissoc params :recursive))
        directory-rows (db/get in-params)
        parent-id (:parent-id in-params)]
    (directory-rows->nested-directories {} parent-id directory-rows)))


(defn get-one-item [params]
  (let [in-params (keyset-to-uuid params [:id :org-id])
        directory (first (db/get in-params))]
    (assoc directory :directories (get-list (clojure.set/rename-keys in-params {:id :parent-id})))))


(defn create [params]
  (let [req-params (select-keys params [:org-id :created-by-id :parent-id :name])
        in-params (keyset-to-uuid req-params [:org-id :parent-id])]
    (res/response (db/create in-params))))
