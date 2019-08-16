(ns nnts2.server
  (:require [nnts2.handler :refer [handler]]
            [ring.adapter.jetty :refer [run-jetty]]
            [nnts2.config :refer [server-spec]]))

(defn start []
  (let [port (Integer/parseInt (:port (server-spec)))
        host (:ip (server-spec))]
    (run-jetty (handler) {:port  port
                          :host  host
                          :join? false})))
