(ns nnts2.routes.user
  (:require [compojure.api.sweet :refer [context POST GET resource defroutes]]
            [nnts2.handler.user :as handler]
            [ring.util.response :as res]
            [clojure.spec.alpha :as s]
            [spec-tools.spec :as spec]))


(defroutes routes
  (GET "/user" []
    handler/create)
  (GET "/user-info" []
    #(res/response (get % :google-user))))
