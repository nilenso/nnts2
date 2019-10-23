(ns nnts2.db-utils
  (:require [nnts2.utils :as utils]
            [honeysql.helpers :as h]
            [honeysql.core :as sql]))

(defn clojure-data->sql-data [sub-form]
  (into {} (for [[k v] sub-form]
             [(utils/kebab->snake k) v])))

(defn get-honeysql-filter-params
  "modified input params to honeysql format where parameters"
  [params]
  (for [[k v] params]
    (if (= k :in)
      [:in [v]]
      [:= k v])))

(defn cast-to-enum [data column-to-enum-map]
  "cast column names to enum type in data"
  (reduce
   (fn [acc [enum-column-name enum-type]]
     (if (enum-column-name acc)
       (assoc
        acc
        enum-column-name
        (sql/call :cast (enum-column-name data) enum-type))
       acc))
   data
   column-to-enum-map))

(defn multi-param-where [input-honey-sql-map where-params]
  (apply h/where input-honey-sql-map
         (get-honeysql-filter-params where-params)))
