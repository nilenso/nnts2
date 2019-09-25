(ns nnts2.db.note
  (:require [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [nnts2.db-utils :as du]
            [nnts2.config :as config]
            [nnts2.utils :as utils]))


(defn create
  ([note-data] (create note-data config/db-spec))
  ([note-data db-spec]
   (let [sql-data (du/clojure-data->sql-data note-data)]
     (-> (jdbc/query (db-spec) (-> (h/insert-into :notes)
                                   (h/values [sql-data])
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers utils/snake->kebab})
         first))))



(defn get
  "list api for notes"
  ([params] (get params config/db-spec))
  ([params db-spec]
   (let [where-params (du/get-honeysql-filter-params params)]
     (jdbc/query (db-spec) (-> (h/select :*)
                               (h/from :notes)
                               (du/multi-param-where where-params)
                               (h/order-by [:created-at :desc])
                               (sql/format))
                 {:identifiers utils/snake->kebab}))))
