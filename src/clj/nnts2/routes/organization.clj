(ns nnts2.routes.organization
  (:require [nnts2.handler.organization :as handler]
            [compojure.core :refer [defroutes GET POST context]]
            [nnts2.routes.directory :as directory]))

(defroutes routes
  (POST "/" [] handler/create)
  (GET "/" [] handler/get-orgs))
