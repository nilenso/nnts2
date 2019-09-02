(ns nnts2.db.organization
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :as config]
            [nnts2.utils :as utils]
            [clojure.java.jdbc :as jdbc]))

(defn insert
  ([details] (insert details config/db-spec))
  ([details db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :organizations)
                                 (h/values [details])
                                 (ph/on-conflict :slug)
                                 (ph/do-nothing)
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers utils/snake->kebab})
       first)))
