(ns nnts2.db.directory
  (:require [nnts2.config :as config]
            [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [honeysql.core :as sql]
            [nnts2.utils :as utils]
            [clojure.java.jdbc :as jdbc]))


#_(defn create)

(defn get
  ([params] (get params config/db-spec))
  ([params db-spec] "something"
   ))


(defn create
  ([params] (create params config/db-spec))
  ([params db-spec]
   (prn params)
   (-> (jdbc/query (db-spec) (-> (h/insert-into :directories)
                                 (h/values [params])
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers utils/snake->kebab})
       first)))
