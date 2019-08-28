(ns nnts2.organization.handler
  (:require [nnts2.organization.db :as db]))

(defn create [{:keys [body] :as request}]
  (prn "REQUEST " request)
  (db/insert body))
