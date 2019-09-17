(ns nnts2.model.directory
  (:require [nnts2.db.directory :as db]
            [compojure.coercions :refer [as-uuid]]
            [ring.util.response :as res]))

(defn dir-rows-to-map [dir-rows]
  "id,child id,parent id, name")

;;also do all string to uuid conversion here
(defn to-uuid- [val]
  (cond
    (nil? val) val
    (uuid? val) val  ;;should remove this co
    :else (as-uuid val)))


(defn keyset-to-uuid [map [k & more]]
  (if k (recur (assoc map k (to-uuid- (get map k))) more) map))


(defn get [params]
  (let [in-params (keyset-to-uuid params [:org-id :parent-id])
        directory-rows (db/get in-params)]
    "dfomadf"))
                                        ;the org id is being checked at handler level, in get fn ehre... no additional checks. we can convert sql data into clojure map here.
;;check if (if given) dir id exists?

;in create fn, check if there is parent dir given, if it exists

(defn create [params]
  (prn "prn create im model" params)
  (let [in-params (keyset-to-uuid params [:org-id :parent-id])
        ]
    (prn "indiseee")
    (res/response (db/create in-params))))
