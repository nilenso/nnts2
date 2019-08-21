(ns nnts2.middleware
  (:require [clojure.walk :as walk]
            [clojure.string :as str]))

(defn snake->kebab
  [data]
  (let [snake->kebab #(-> % str (str/replace "_" "-") (subs 1) keyword)
        transform-map (fn [form]
                        (if (map? form)
                          (reduce-kv #(assoc %1 (snake->kebab %2) %3) {} form)
                          form))]
    (walk/postwalk transform-map data)))


(defn wrap-kebab-case
  [handler]
  (fn [request]
    (let [kebab-request (select-keys request [:session :params :body])]
      (-> request
          (dissoc [:session :params :body])
          (conj (snake->kebab kebab-request))
          (handler)))))

