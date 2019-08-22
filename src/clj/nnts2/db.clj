(ns nnts2.db
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [nnts2.config :refer [db-spec]]))

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
