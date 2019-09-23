(ns nnts2.model.member-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(s/def ::org-id uuid?)
(s/def ::user-id uuid?)
(s/def ::role #{"admin" "member" "guest"})
(s/def ::members (s/keys :req-un [::org-id ::user-id ::role]))

(defn valid? [m]
  (s/valid? ::members m))

(defn explain-str [m]
  (s/explain-data ::members m))
