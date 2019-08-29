(ns nnts2.note.db
  (:require [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [nnts2.middleware :refer [snake->kebab kebab->snake]]
            [nnts2.config :as config]))

;; just change keywords from kebab to snake

(defn clojure-data->sql-data
  "this seems like its already being done by honeysql"
  [sub-form]
  (into {} (for [[k v] sub-form]
             [(kebab->snake k) v])))

(defn create
  ([note-data] (create note-data config/db-spec))
  ([note-data db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :notes)
                                 (h/values [note-data])
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers snake->kebab})
       first)))


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
                               (sql/format))
                 {:identifiers snake->kebab}))))
