(ns nnts2.db.organization
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :as config]
            [nnts2.utils :as utils]
            [clojure.java.jdbc :as jdbc]))

(defn create
  ([details] (create details config/db-spec))
  ([details db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :organizations)
                                 (h/values [details])
                                 (ph/on-conflict :slug)
                                 (ph/do-nothing)
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
                         (ph/on-conflict-constraint :unique_org_id_user_id)
                         (ph/do-nothing)
                         (ph/returning :*)
                         sql/format)
                     {:identifiers utils/snake->kebab})
         first))))

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

(defn get
  ;todo -- use the middleware created for note where params
  ([params] (get params config/db-spec))
  ([params db-spec]
   (jdbc/query (db-spec)
               (-> (h/select :*)
                   (h/from [:members :m])
                   (h/where [:= :m.user-id (:nnts-user params)] [:= :m.org-id (:org-id params)])
                   sql/format)
               {:identifiers utils/snake->kebab})))
