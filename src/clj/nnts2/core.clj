(ns nnts2.core
  (:require [nnts2.config :as config]
            [nnts2.server :as server]
            [nnts2.db :as db]
            [nnts2.scripts.setup :as script-setup])
  (:gen-class))

(defn stop! []
  (db/rollback)
  (server/stop))

(defn- start! []
  (db/migrate)
  (server/start))

(defn restart-server! []
  (stop!)
  (start!))

(defn -main
  [& args]
  (cond
    (= (first args) "dev")  (config/read :dev)
    (= (first args) "test") (config/read :test)
    :else                   (config/read :prod))
  (cond
    (= (second args) "migrate")  (db/migrate)
    (= (second args) "rollback") (db/rollback)
    (= (second args) "script")   (script-setup/call-script (first (rest (rest args))))
    :else                        (start!)))
