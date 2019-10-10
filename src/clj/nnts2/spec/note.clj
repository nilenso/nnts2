(ns nnts2.spec.note
  (:require [clojure.spec.alpha :as s]))

(s/def ::non-empty-string (s/and string? #(not (empty? %))))
(s/def ::uuid uuid?)

(s/def ::title ::non-empty-string)
(s/def ::content ::non-empty-string)
(s/def ::directory-id ::uuid)

(s/def ::note (s/keys :req-un [::title ::content]))
