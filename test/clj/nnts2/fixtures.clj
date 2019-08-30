(ns nnts2.fixtures
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as sql]
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
    (with-redefs [config/db-spec (constantly db)]
      (test))))
