(ns nnts2.db-utils
  (:require [nnts2.utils :as utils]))


(defn clojure-data->sql-data [sub-form]
  (into {} (for [[k v] sub-form]
             [(utils/kebab->snake k) v])))

(defn get-honeysql-filter-params
  "modified input params to honeysql format where parameters"
  [params]
  (for [[k v] params] [:= k v]))

(defn multi-param-where [input-honey-sql-map where-params]
  (apply honeysql.helpers/where input-honey-sql-map
         (get-honeysql-filter-params where-params)))
