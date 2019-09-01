(ns nnts2.organization.db
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :refer [db-spec]]
            [nnts2.middleware :refer [snake->kebab]]
            [clojure.java.jdbc :as jdbc]))

(defn insert
  ([details] (insert details db-spec))
  ([details db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :organizations)
                                 (h/values [details])
                                 (ph/on-conflict :slug)
                                 (ph/do-nothing)
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers snake->kebab})
       first)))
