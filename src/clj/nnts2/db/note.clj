(ns nnts2.db.note
  (:require [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [nnts2.utils :as utils]
            [nnts2.config :as config]))

;; just change keywords from kebab to snake

(defn clojure-data->sql-data [sub-form]
  (into {} (for [[k v] sub-form]
             [(utils/kebab->snake k) v])))

(defn add
  ([note-data] (add note-data config/db-spec))
  ([note-data db-spec]
   (let [sql-data (clojure-data->sql-data note-data)]
     (-> (jdbc/query (db-spec) (-> (h/insert-into :notes)
                                   (h/values [sql-data])
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers utils/snake->kebab})
         first))))
