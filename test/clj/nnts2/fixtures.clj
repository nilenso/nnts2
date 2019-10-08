(ns nnts2.fixtures
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as sql]
            [honeysql.helpers :as h]
            [honeysql.core :as hcore]
            [nnts2.config :as config]
            [nnts2.db :as db])
  (:import (java.util UUID)))

(def ^:private test-specs (atom nil))

(defn setup
  [tests]
  (binding [config/db-spec #(:db-spec @test-specs)]
    (config/read :test test-specs)
    (db/migrate)
    (tests)))

(defn clear [test]
  (sql/with-db-transaction [db (config/db-spec)]
    (sql/db-set-rollback-only! db)
    (binding [config/db-spec (constantly db)]
      (test))))

(defn adduser
  [tests]
  (sql/execute! (config/db-spec) (-> (h/insert-into :users)
                                     (h/values [{:email      "temp@gmail.com"
                                                 :id         (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff")
                                                 :first_name "temp"
                                                 :last_name  "temp"}])
                                     hcore/format))
  (tests)
  (sql/execute! (config/db-spec) (-> (h/delete-from :users)
                                     hcore/format)))
