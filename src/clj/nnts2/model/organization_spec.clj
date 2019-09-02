(ns nnts2.model.organization-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(s/def ::slug (s/and str/lower-case string?))
(s/def ::name string?)
(s/def ::organizations (s/keys :req-un [::name ::slug]))

(defn valid? [org-details]
  (s/valid? ::organizations org-details))

(defn explain-str [org-details]
  (s/explain-data ::organizations org-details))
