(ns nnts2.routes.organization
  (:require [nnts2.handler.organization :as handler]
            [compojure.core :refer [defroutes GET POST]]))

(defroutes routes
           (POST "/org" [] handler/create)
           (GET "/orgs" [] handler/get-orgs))