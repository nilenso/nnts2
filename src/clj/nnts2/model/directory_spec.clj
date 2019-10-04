(ns nnts2.model.directory-spec
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(def alnum-regex #"[a-zA-Z0-9-]+")
(def uuid-regex #"^[0-9a-f]{8}-?[0-9a-f]{4}-?[1-5][0-9a-f]{3}-?[89ab][0-9a-f]{3}-?[0-9a-f]{12}$")

(s/def ::alphanumeric (s/and string?
                             #(> (count %)0)
                             #(re-matches alnum-regex %)))

(s/def ::is-uuid (s/or
                  :type-uuid uuid?
                  :str-uuid #(re-matches uuid-regex %)))


(s/def ::name ::alphanumeric)
(s/def ::org-id ::is-uuid)
(s/def ::parent-id (s/or :nil nil? :uuid ::is-uuid))


(s/def ::directory (s/keys :req-un [::name ::org-id]
                           :opt-un [::parent-id]))

(defn valid? [dir-details]
  (s/valid? ::directory dir-details))

(defn explain-str? [dir-details]
  (s/explain-str ::directory dir-details))
