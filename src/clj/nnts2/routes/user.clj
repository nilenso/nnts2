(ns nnts2.routes.user
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :as res]
            [nnts2.handler.user :as user-handler]))

(defroutes routes
           (GET "/user" [] user-handler/create)

           (GET "/user-info" []
              #(res/response (get % :google-user))))
