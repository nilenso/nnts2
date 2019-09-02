(ns nnts2.routes.organization
  (:require [nnts2.handler.organization :as handler]
            [compojure.core :refer [defroutes POST]]))

(defroutes routes
  (POST "/org" [] handler/create))
