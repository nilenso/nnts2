(ns nnts2.db.directory
  (:require [nnts2.config :as config]
            [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [honeysql.core :as sql]
            [nnts2.utils :as utils]
            [clojure.java.jdbc :as jdbc]))


(defn multi-where [in-honey-sql-map where-params]
  (reduce  (fn [acc b] (h/merge-where acc [:= (key b) (val b)])) in-honey-sql-map where-params))


(defn get
  ([where-param-map] (get where-param-map config/db-spec))
  ([where-param-map db-spec]
   (-> (jdbc/query (db-spec) (-> (h/select :id :name :parent-id)
                                 (h/from [:directories :d])
                                 (multi-where where-param-map)
                                 sql/format)
                   {:identifiers utils/snake->kebab}))))

(defn debug [x] (prn x) x)
(defn create
  ([params] (create params config/db-spec))
  ([params db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :directories)
                                 (h/values [params])
                                 (ph/returning :*)
                                 sql/format
                                 debug)
                   {:identifiers utils/snake->kebab})
       first)))
