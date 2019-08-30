(ns nnts2.member.db
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :refer [db-spec]]
            [nnts2.middleware :refer [snake->kebab]]
            [clojure.java.jdbc :as jdbc]))

(defn debug [x] (prn x) x)

(defn add
  ([data] (add data db-spec))
  ([data db-spec]
   (let [casted-data (assoc data :role (sql/call :cast (:role data) :role))]
     (-> (jdbc/query (db-spec)
                     (-> (h/insert-into :members)
                         (h/values [casted-data])
                         (ph/on-conflict-constraint :unique_org_id_user_id)
                         (ph/do-nothing)
                         (ph/returning :*)
                         sql/format)
                     {:identifiers snake->kebab})
         first))))

(defn get-orgs
  ([id] (get-orgs id nil))
  ([id db-spec]
   (jdbc/query (db-spec)
               (-> (h/select :*)
                   (h/from :members)
                   (h/where [:= :id id])
                   sql/format)
               {:identifiers snake->kebab})))
