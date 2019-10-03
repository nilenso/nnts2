(ns nnts2.model.note
  (:require [clojure.spec.alpha :as s]))


(s/def ::non-empty-string (s/and string? #(not (empty? %))))

(s/def ::title ::non-empty-string)
(s/def ::content ::non-empty-string)
(s/def ::created-by-id uuid?)
(s/def ::directory-id uuid?)
(s/def ::note (s/keys :req-un [::title ::content ::created-by-id ::directory-id]))


(defn valid? [note-data]
  (s/valid? ::note note-data))

(defn explain-str [note-data]
  (s/explain-str ::note note-data))
