(ns nnts2.model.user
  (:require [clojure.spec.alpha :as s]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(s/def ::email-type (s/and string? #(re-matches email-regex %)))

(def url-regex #"^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$")
(s/def ::url (s/and string? #(re-matches url-regex %)))

(def name-regex #"^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")
(s/def ::name (s/and string? #(> (count %) 0) #(re-matches name-regex %)))

(s/def ::email ::email-type)
(s/def ::given-name ::name)
(s/def ::family-name ::name)
(s/def ::picture ::url)

(s/def ::users (s/keys :req-un [::email ::given-name ::family-name]
                       :opt-un [::picture]))

(defn valid? [user-info]
  (s/valid? ::users user-info))

(defn explain-str [user-info]
  (s/explain-data ::users user-info))
