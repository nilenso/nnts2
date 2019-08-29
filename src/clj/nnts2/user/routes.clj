(ns nnts2.user.routes
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :as res]
            [nnts2.user.handler :as handler]))

(defroutes routes
  (GET "/user" [] handler/create)

  (GET "/user-info" []
       #(res/response (get-in % [:session :user-info]))))
