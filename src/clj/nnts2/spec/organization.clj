(ns nnts2.spec.organization
  (:require [clojure.spec.alpha :as s]))

(s/def ::name string?)
(s/def ::slug string?)
(s/def ::organization (s/keys :req-un [::name ::slug]))
