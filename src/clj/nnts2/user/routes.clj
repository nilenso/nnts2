(ns nnts2.user.routes
  (:require [compojure.core :refer [defroutes GET]]
            [nnts2.user.handler :refer [add]]
            [nnts2.user.middleware :refer [oauth2-user-info]]
            ))

(defn wrap [handler]
  (-> handler
      (oauth2-user-info)))

(defroutes routes
           (GET "/user" []
                ;:middlewares [oauth2-user-info]
                (wrap add)))

;(def routes)

#_(defroutes* routes
  (context "/user" []
           (GET* "" []
                :middlewares [oauth2-user-info]
                add)))
