(ns nnts2.spec.invitation
  (:require [clojure.spec.alpha :as s]))


(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(s/def ::email-type (s/and string? #(re-matches email-regex %)))

(s/def ::role #{"admin" "member" "guest"})
(s/def ::invite-status #{"accepted" "rejected"})

(s/def ::org-id uuid?)
(s/def ::invite-id uuid?)
(s/def ::email ::email-type)
(s/def ::invite-for-role ::role)
(s/def ::accept-or-rejct ::invite-status)
(s/def ::status (s/and string? #(= "cancelled" %)))


(s/def ::invitation (s/keys :req-un [::email ::invite-for-role]))

(s/def ::update-invitation (s/keys :opt-un [::email ::invite-for-role ::status]))
