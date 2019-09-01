(ns nnts2.organization.routes
  (:require [nnts2.organization.handler :as handler]
            [compojure.core :refer [defroutes POST GET]]))

(defroutes routes
           (POST "/org" [] handler/create)
           (GET "/org" [] handler/get-orgs))
