(ns nnts2.organization.db
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :refer [db-spec]]
            [nnts2.middleware :refer [snake->kebab]]
            [clojure.java.jdbc :as jdbc]))

(defn create
  ([details] (create details db-spec))
  ([details db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :organizations)
                                 (h/values [details])
                                 (ph/on-conflict :slug)
                                 (ph/do-nothing)
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers snake->kebab})
       first)))

(defn add-user
  ([data] (add-user data db-spec))
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

(defn get-by-user-id
  ([user-id] (get-by-user-id user-id db-spec))
  ([user-id db-spec]
   (jdbc/query (db-spec)
               (-> (h/select :name :slug :org.id)
                   (h/from [:members :m])
                   (h/join [:organizations :org] [:= :org.id :m.org-id])
                   (h/where [:= :m.user-id user-id])
                   sql/format)
               {:identifiers snake->kebab})))

