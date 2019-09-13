(ns nnts2.model.directory-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(def alnum-regex #"[a-zA-Z0-9]+")
(s/def ::alphanumeric (s/and string?
                             #(> (count %)0)
                             #(re-matches alnum-regex %)))

(s/def ::name ::alphanumeric)
(s/def ::path #(s/valid? ::alphanumeric (str/replace % "." "")))

(s/def ::directory (s/keys req-un [::name ::path]))

(defn valid? [dir-details]
  (s/valid? ::directory dir-details))
