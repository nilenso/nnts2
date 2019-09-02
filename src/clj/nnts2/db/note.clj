(ns nnts2.db.note
  (:require [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [nnts2.db-middleware :as middleware]
            [nnts2.config :as config]
            [nnts2.utils :as utils]))

(defn add
  ([note-data] (add note-data config/db-spec))
  ([note-data db-spec]
   (let [sql-data (middleware/clojure-data->sql-data note-data)]
     (-> (jdbc/query (db-spec) (-> (h/insert-into :notes)
                                   (h/values [sql-data])
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers utils/snake->kebab})
         first))))
