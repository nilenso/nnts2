(ns nnts2.user.routes
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :as res]
            [nnts2.user.handler :refer [create info]]))

(defroutes routes
           (GET "/user" [] create)

           (GET "/user-info" []
              #(res/response (get % :google-user))))
