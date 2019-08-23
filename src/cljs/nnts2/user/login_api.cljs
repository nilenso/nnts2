(ns nnts2.user.login-api
  (:require [ajax.core :refer [GET POST]]))

(defn login []
  (GET "/login"
       {:response-format :json
        :keywords?       true}))