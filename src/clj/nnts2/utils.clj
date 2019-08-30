(ns nnts2.utils
  (:require [clojure.string :as str]
           [clojure.walk :as walk]))

(defn snake->kebab
  [word]
  (-> word keyword str (str/replace "_" "-") (subs 1) keyword))

(defn kebab->snake
  [word]
  (-> word keyword str (str/replace "-" "_") (subs 1) keyword))

(defn hyphenize-collection
  [data]
  (let [transform-map (fn [form]
                        (if (map? form)
                          (reduce-kv #(assoc %1 (snake->kebab %2) %3) {} form)
                          form))]
    (walk/postwalk transform-map data)))
