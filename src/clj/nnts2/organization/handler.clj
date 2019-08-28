(ns nnts2.organization.handler
  (:require [nnts2.organization.db :as db]))

(defn create [{:keys [params] :as request}]
  (prn request)
  (db/insert (dissoc params :*)))
