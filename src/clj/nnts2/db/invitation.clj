(ns nnts2.db.invitation
  (:require [nnts2.config :as config]
            [clojure.java.jdbc :as jdbc]
            [honeysql.helpers :as h]
            [nnts2.db-utils :as db-utils]
            [honeysql.core :as sql]
            [nnts2.utils :as utils]
            [honeysql-postgres.helpers :as ph]))

(def invitation-column-to-enum-list {:invite-for-role :role
                                     :status          :invite-status})

(defn cast-invite-data [data]
  (db-utils/cast-to-enum data invitation-column-to-enum-list))

(defn get
  ([where-params-map] (get where-params-map config/db-spec))
  ([where-params-map db-spec]
   (let [casted-where-map (cast-invite-data where-params-map)]
     (-> (jdbc/query (db-spec) (-> (h/select :email :org-id :status)
                                   (h/from [:invitation :i])
                                   (db-utils/multi-param-where casted-where-map)
                                   sql/format)
                     {:identifiers utils/snake->kebab})))))

(defn create
  ([params] (create params config/db-spec))
  ([params db-spec]
   (let [casted-data (cast-invite-data params)]
     (-> (jdbc/query (db-spec) (-> (h/insert-into :invitation)
                                   (h/values [casted-data])
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers utils/snake->kebab})
         first))))

(defn update
  ([update-values where-values] (update update-values where-values config/db-spec))
  ([update-values where-values db-spec]
   (let [casted-update-values (cast-invite-data update-values)
         casted-where-values  (cast-invite-data where-values)]
     (-> (jdbc/query (db-spec) (-> (h/update :invitation)
                                   (h/sset casted-update-values)
                                   (db-utils/multi-param-where casted-where-values)
                                   (ph/returning :*)
                                   sql/format)
                     {:identifiers utils/snake->kebab})))))
