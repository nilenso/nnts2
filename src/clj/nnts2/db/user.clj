(ns nnts2.db.user
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :as config]
            [nnts2.utils :as utils]
            [clojure.java.jdbc :as jdbc]))

(defn create
  ([user] (create user config/db-spec))
  ([{:keys [email given-name family-name picture]} db-spec]
   (-> (jdbc/query (db-spec) (-> (h/insert-into :users)
                                 (h/values [{:email      email
                                             :first_name given-name
                                             :last_name  family-name
                                             :image_url  picture}])
                                 (ph/upsert (-> (ph/on-conflict :email)
                                                (ph/do-update-set :first_name :last_name :image_url)))
                                 (ph/returning :*)
                                 sql/format)
                   {:identifiers utils/snake->kebab})
       first)))

(defn get-by-email
  ([email] (get-by-email email config/db-spec))
  ([email db-spec]
   (-> (jdbc/query (db-spec) (-> (h/select :*)
                                 (h/from :users)
                                 (h/where [:= :email email])
                                 sql/format)
                   {:identifiers utils/snake->kebab})
       first)))

(defn get-by-id
  ([id] (get-by-id id config/db-spec))
  ([id db-spec]
   (-> (jdbc/query (db-spec) (-> (h/select :*)
                                 (h/from :users)
                                 (h/where [:= :id id])
                                 sql/format)
                   {:identifiers utils/snake->kebab})
       first)))
