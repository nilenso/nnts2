(ns nnts2.core
  (:require [nnts2.config :as config]
            [nnts2.server :as server]))

(defn- start
  [config-params]
  (server/start (:server-spec config-params)))

(defn -main
  [& args]
  (let [config-params (cond
                        (= (first args) "dev") (config/read :dev)
                        (= (first args) "test") (config/read :test))]
    (start config-params)))
