(ns nnts2.db.directory
  (:require [nnts2.config :as config]))


#_(defn create)

(defn get
  ([params] (get params config/db-spec))
  ([params db-spec] "soemthing"))


(defn create
  ([params] (create params config/db-spec))
  ([params db-spec] "create"))
