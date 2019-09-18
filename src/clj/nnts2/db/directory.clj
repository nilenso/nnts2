(ns nnts2.db.directory
  (:require [nnts2.config :as config]
            [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [honeysql.core :as sql]
            [nnts2.utils :as utils]
            [clojure.java.jdbc :as jdbc]))


(defn get-filter-params [map params]
  (reduce  (fn [acc b] (h/merge-where acc [:= (key b) (val b)])) map params))


(defn get
  ([params] (get params config/db-spec))
  ([params db-spec]
   (-> (jdbc/query (db-spec) (-> (h/select :id :name :parent-id)
                                 (h/from [:directories :d])
                                 (get-filter-params params)
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
