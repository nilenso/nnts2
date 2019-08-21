(ns nnts2.user.spec
  (:require [clojure.spec.alpha :as s]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(s/def ::email-type (s/and string? #(re-matches email-regex %)))

(def url-regex #"/((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/")
(s/def ::url (s/and string? #(re-matches url-regex %)))

(s/def ::email ::email-type)
(s/def ::given-name string?)
(s/def ::family-name string?)
(s/def ::picture ::url)

(s/def ::users {:req [::email ::given-name ::family-name ::picture]})

(defn valid? [user-info]
  (s/valid? ::users user-info))

(defn explain-str [user-info]
  (s/explain-str ::users user-info))
