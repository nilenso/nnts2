(ns nnts2.routes.organization
  (:require [compojure.api.sweet :refer [context POST GET resource defroutes]]
            [nnts2.handler.organization :as handler]
            [clojure.spec.alpha :as s]))


(s/def ::name string?)
(s/def ::slug string?)
(s/def ::organization (s/keys :req-un [::name ::slug]))


(defroutes routes
  (context "/org" []
    :coercion :spec
    (GET "/" [] handler/get-orgs)
    (POST "/" []
      :body [org-details ::organization]
      #(handler/create % org-details))))
