(ns nnts2.spec.directory
  (:require [clojure.spec.alpha :as s]))


(def alnum-regex #"[a-zA-Z0-9-]+")

(s/def ::uuid uuid?)
(s/def ::name (s/and string? #(re-matches alnum-regex %)))
(s/def ::parent-id (s/or :uuid ::uuid :nil nil?))
(s/def ::org-id ::uuid)
(s/def ::show-tree boolean?)

(s/def ::directory (s/keys :opt-un [::parent-id]
                           :req-un [::name]))

(defn valid? [data] (s/valid? ::directory data))
(defn org-valid? [data] (s/valid? ::org-id data))
(defn explain [data] (s/explain-data ::directory data))
