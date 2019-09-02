(ns nnts2.routes.note
  (:require [compojure.core :refer [GET POST defroutes]]
            [nnts2.handler.note :as handler]))

(defroutes routes
    (POST "/create" []
       handler/create))
