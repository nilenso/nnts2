(ns nnts2.model.directory
  (:require [nnts2.db.directory :as db]))

(defn get [params]
  (let [directory-rows (db/get params)]
    "dir rows"))
                                        ;the org id is being checked at handler level, in get fn ehre... no additional checks. we can convert sql data into clojure map here.
;;check if (if given) dir id exists?

;in create fn, check if there is parent dir given, if it exists
