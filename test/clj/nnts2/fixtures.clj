(ns nnts2.fixtures
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as sql]
            [honeysql.helpers :as h]
            [honeysql-postgres.helpers :as ph]
            [honeysql.core :as hcore]
            [nnts2.config :as config]
            [nnts2.db :as db]))


(defn setup
  [tests]
  (config/read :test)
  (db/migrate)
  (tests))

(defn clear [test]
  (sql/with-db-transaction [db (config/db-spec)]
                           (sql/db-set-rollback-only! db)
                           (binding [config/db-spec (constantly db)]
                             (test))))



(defn adduser
  [tests]
  (sql/execute! (config/db-spec) (-> (h/insert-into :users)
                                 (h/values [{:email "temp@gmail.com" :id 1}])
                                 hcore/format))
  (tests)
  (sql/execute! (config/db-spec) (-> (h/delete-from :users)
                                     hcore/format)))
