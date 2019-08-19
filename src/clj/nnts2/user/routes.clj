(ns nnts2.user.routes
  (:require [compojure.core :refer [defroutes GET]]
            [nnts2.user.handler :refer [add]]))

(defroutes routes
           (GET "/user" [] add))