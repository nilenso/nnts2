(ns nnts2.db.directory
  (:require [nnts2.config :as config]
            [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [honeysql.core :as sql]
            [clojure.java.jdbc :as jdbc]
            [nnts2.utils :as utils]
            [nnts2.db-utils :as db-utils]))

(defn get
  ([] (get {}))
  ([where-param-map] (get where-param-map config/db-spec))
  ([where-param-map db-spec]
   (-> (jdbc/query (db-spec) (-> (h/select :id :name :parent-id :org-id)
                                 (h/from [:directories :d])
                                 (db-utils/multi-param-where where-param-map)
                                 sql/format)
                   {:identifiers utils/snake->kebab}))))

(defn create
  ([params] (create params config/db-spec))
  ([params db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :directories)
                                 (h/values [params])
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers utils/snake->kebab})
       first)))
