(ns nnts2.core
  (:require [nnts2.config :as config]
            [nnts2.server :as server]
            [nnts2.db :as db])
  (:gen-class))

(defn stop! []
  (db/rollback)
  (server/stop))

(defn- start! []
  (db/migrate)
  (server/start))

(defn -main
  [& args]
  (cond
    (= (first args) "dev") (config/read :dev)
    (= (first args) "test") (config/read :test)
    :else (config/read :prod))
  (cond
    (= (second args) "migrate") (db/migrate)
    (= (second args) "rollback") (db/rollback)
    :else (start!)))
