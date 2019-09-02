(ns nnts2.db-middleware
  (:require [nnts2.utils :as utils]))


(defn clojure-data->sql-data [sub-form]
  (into {} (for [[k v] sub-form]
             [(utils/kebab->snake k) v])))
