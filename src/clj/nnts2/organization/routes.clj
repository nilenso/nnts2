(ns nnts2.organization.routes
  (:require [nnts2.organization.handler :as handler]
            [compojure.core :refer [defroutes POST]]))

(defroutes routes
  (POST "/create-organization" [] handler/create))
