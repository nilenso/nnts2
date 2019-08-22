(ns nnts2.user.routes
  (:require [compojure.core :refer [defroutes GET]]
            [nnts2.user.handler :refer [create]]
            [nnts2.user.middleware :refer [oauth2-user-info]]))

(defn wrap [handler]
  (-> handler
      (oauth2-user-info)))

(defroutes routes
           (GET "/user" []
             (wrap create)))
