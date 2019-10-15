(ns nnts2.routes.organization
  (:require [compojure.api.sweet :refer [context POST GET resource defroutes]]
            [nnts2.handler.organization :as handler]
            [nnts2.spec.organization :as spec]))

(defroutes routes
  (context "/orgs" []
    :coercion :spec
    (GET "/" []
      handler/get-orgs-for-member)
    (POST "/" []
      :body [org-details ::spec/organization]
      #(handler/create % org-details))))
