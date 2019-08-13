(ns nnts2.config
 (:require [aero.core :as aero]))

(def db-spec (atom nil))
(def server-spec (atom nil))

(defn read 
  "Use the provided environment to get it's profile"
  [env]
  (let [config (aero/read-config "config/config.edn" {:profile env})]
    (reset! db-spec (:db-spec config))
    (reset! server-spec (:server-spec config))
    config))

(read :dev)
