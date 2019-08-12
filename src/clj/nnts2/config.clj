(ns nnts2.config
 (:require [aero.core :as aero]))

(defn read 
  "Use the provided environment to get it's profile"
  [env]
  (aero/read-config "config/config.edn" {:profile env}))

(read :dev)
