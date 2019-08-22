(ns nnts2.middleware
  (:require [clojure.walk :as walk]
            [clojure.string :as str]))

(defn snake->kebab
  [word]
  (-> word
      (name)
      (str/replace "_" "-")
      (keyword)))

(defn create-transform-map
  [data]
  (if (map? data)
    (reduce-kv #(assoc %1 (snake->kebab %2) %3) {} data)
    data))

(defn wrap-kebab-case
  [handler]
  (fn [request]
    (let [kebab-request (select-keys request [:session :params :body])]
      (-> request
          (dissoc [:session :params :body])
          (conj (walk/postwalk create-transform-map kebab-request))
          (handler)))))

