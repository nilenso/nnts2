(ns nnts2.db.organization
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :as config]
            [nnts2.utils :as utils]
            [nnts2.db-utils :as db-utils]
            [clojure.java.jdbc :as jdbc]))

(defn create
  ([details] (create details config/db-spec))
  ([details db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :organizations)
                                 (h/values [details])
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers utils/snake->kebab})
       first)))

(defn add-user
  ([data] (add-user data config/db-spec))
  ([data db-spec]
   (let [casted-data (assoc data :role (sql/call :cast (:role data) :role))]
     (-> (jdbc/query (db-spec)
                     (-> (h/insert-into :members)
                         (h/values [casted-data])
                         (ph/on-conflict-constraint :unique-org-id-user-id)
                         (ph/do-nothing)
                         (ph/returning :*)
                         sql/format)
                     {:identifiers utils/snake->kebab})
         first))))

(defn get
  ([where-param-map] (get where-param-map config/db-spec))
  ([where-param-map db-spec]
   (jdbc/query (db-spec)
               (-> (h/select :*)
                   (h/from :organizations)
                   (db-utils/multi-param-where where-param-map)
                   sql/format)
               {:identifiers utils/snake->kebab})))

(defn get-by-user-id
  ([user-id] (get-by-user-id user-id config/db-spec))
  ([user-id db-spec]
   (jdbc/query (db-spec)
               (-> (h/select :name :slug :org.id)
                   (h/from [:members :m])
                   (h/join [:organizations :org] [:= :org.id :m.org-id])
                   (h/where [:= :m.user-id user-id])
                   sql/format)
               {:identifiers utils/snake->kebab})))

(defn get-membership
  ([where-param-map] (get-membership where-param-map config/db-spec))
  ([where-param-map db-spec]
   (jdbc/query (db-spec)
               (-> (h/select :*)
                   (h/from :members)
                   (h/join [:organizations :org] [:= :org.id :org-id])
                   (db-utils/multi-param-where where-param-map)
                   sql/format)
               {:identifiers utils/snake->kebab})))
