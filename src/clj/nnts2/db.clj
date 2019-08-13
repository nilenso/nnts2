(ns nnts2.db
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [nnts2.config :refer [db-spec]]))

(defn- load-config 
  []
  {:datastore  db-spec
   :migrations (jdbc/load-resources "migrations")})

(defn migrate
  []
  (repl/migrate (load-config)))

(defn rollback 
  []
  (repl/rollback (load-config db-spec)))
