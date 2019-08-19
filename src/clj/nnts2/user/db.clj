(ns nnts2.user.db
  (:require [honeysql.helpers :as h]
            [honeysql.core :as sql]
            [honeysql-postgres.format :refer :all]
            [honeysql-postgres.helpers :as ph]
            [nnts2.config :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]))

(defn add
  ([user] (add user db-spec))
  ([{:keys [email given_name family_name image_url]} db-spec]
   (jdbc/execute! (db-spec) (-> (h/insert-into :users)
                                (h/values [{:email      email
                                            :first_name given_name
                                            :last_name  family_name
                                            :image_url  image_url}])
                                (ph/returning :*)
                                sql/format))))