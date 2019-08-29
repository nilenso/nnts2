(ns nnts2.member.db
  (:require [honeysql.helper :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :refer [db-spec]]
            [nnts2.middleware :refer [snake->kebab]]
            [clojure.java.jdbc :as jdbc]))

(defn add
  ([id] (add data nil))
  ([data db-spec]
   (-> (jdbc/execute! (db-spec)
                      (-> (h/insert-into :members)
                          (h/values [data])
                          (ph/upsert (-> (ph/on-conflict :user_id)
                                         (ph/do-nothing)))
                          (ph/returning :*)
                          sql/format)
                      {:identifiers snake->kebab})
       first)))

(defn get-orgs
  ([id] (get-orgs id nil))
  ([id db-spec]
   (jdbc/query (db-spec)
               (-> (h/select :*)
                   (h/from :members)
                   (h/where [:= :id id])
                   sql/format)
               {:identifiers snake->kebab})))
