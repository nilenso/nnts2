(ns nnts2.note.db
  (:require [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [nnts2.middleware :refer [snake->kebab kebab->snake]]
            [nnts2.config :refer [db-spec]]))

;; just change keywords from kebab to snake

(defn clojure-data->sql-data [sub-form]
  (into {} (for [[k v] sub-form]
             [(kebab->snake k) v])))

(defn add
  ([note-data] (add note-data db-spec))
  ([note-data db-spec]
   (let [sql-data (clojure-data->sql-data note-data)]
     (-> (jdbc/query (db-spec) (-> (h/insert-into :notes)
                                   (h/values [sql-data])
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers snake->kebab})
         first))))
