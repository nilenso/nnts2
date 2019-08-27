(ns nnts2.db
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [nnts2.config :as config :refer [db-spec]]))

(defn- migration-config
  []
  {:datastore  (jdbc/sql-database (db-spec))
   :migrations (jdbc/load-resources "migrations")})

(defn migrate
  []
  (repl/migrate (migration-config)))

(defn rollback
  []
  (repl/rollback (migration-config)))

(defn lein-migrate-db [& args]
  (config/read (keyword (first args)))
  (migrate))

(defn lein-rollback-db [& args]
  (config/read (keyword (first args)))
  (rollback))
