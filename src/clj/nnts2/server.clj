(ns nnts2.server
  (:require [nnts2.handler :refer [handler]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn start [server-spec]
  (let [port (:port server-spec)
        host (:ip server-spec)]
    (run-jetty handler {:port  port
                        :host  host
                        :join? false})))
