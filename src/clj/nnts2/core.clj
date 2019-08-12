(ns nnts2.core
  (:require [nnts2.config :as config]))


(defn -main
  [& args]
  (cond 
    (= (first args) "dev") (config/read :dev)
    (= (first args) "test") (config/read :test)))
