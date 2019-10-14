(ns nnts2.db.note
  (:require [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [nnts2.db-utils :as du]
            [nnts2.config :as config]
            [nnts2.utils :as utils]))

(defn create
  "create a new note"
  ([note-data] (create note-data config/db-spec))
  ([note-data db-spec]
   (let [sql-data (du/clojure-data->sql-data note-data)]
     (-> (jdbc/query (db-spec) (-> (h/insert-into :notes)
                                   (h/values [sql-data])
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers utils/snake->kebab})
         first))))


(defn update
  "update notes with update-data for rows which satisfy where-params"
  ([update-data where-params] (update update-data where-params config/db-spec))
  ([update-data where-params db-spec]
   (let [sql-data (du/clojure-data->sql-data update-data)]
     (-> (jdbc/query (db-spec) (-> (h/update :notes)
                                   (h/sset sql-data)
                                   (du/multi-param-where where-params)
                                   (ph/returning :*)
                                   sql/format))))))

(defn get
  "list api for notes"
  ([where-params] (get where-params config/db-spec))
  ([where-params db-spec]
   (jdbc/query (db-spec) (-> (h/select :*)
                             (h/from :notes)
                             (du/multi-param-where where-params)
                             (h/order-by [:created-at :desc])
                             (sql/format))
               {:identifiers utils/snake->kebab})))
