(ns nnts2.db.note
  (:require [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [nnts2.db-middleware :as middleware]
            [nnts2.config :as config]
            [nnts2.utils :as utils]))

;; just change keywords from kebab to snake


(defn create
  ([note-data] (create note-data config/db-spec))
  ([note-data db-spec]
   (let [sql-data (middleware/clojure-data->sql-data note-data)]
     (-> (jdbc/query (db-spec) (-> (h/insert-into :notes)
                                   (h/values [sql-data])
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers utils/snake->kebab})
         first))))


(defn get-honeysql-filter-params
  "modified input params to honeysql format where parameters"
  [params]
  (for [[k v] params] [:= k v]))


(defn get
  "list api for notes"
  ([params] (get params config/db-spec))
  ([params db-spec]
   (let [filter-params (get-honeysql-filter-params params)]
     (jdbc/query (db-spec) (-> (h/select :*)
                               (h/from :notes)
                               (#(apply h/where % filter-params))
                               (h/order-by [:created-at :desc])
                               (sql/format))
                 {:identifiers utils/snake->kebab}))))
