(ns nnts2.model.note
  (:require [clojure.spec.alpha :as s]))


(s/def ::title string?)
(s/def ::content string?)
(s/def ::created-by-id uuid?)
(s/def ::note (s/keys :req-un [::title ::content ::created-by-id]))


(defn valid? [note-data]
  (s/valid? ::note note-data))

(defn explain-str [note-data]
  (s/explain-str ::note note-data))
