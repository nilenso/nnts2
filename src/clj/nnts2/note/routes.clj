(ns nnts2.note.routes
  (:require [compojure.core :refer [GET POST defroutes]]))

(defroutes routes
  (GET "/create" []
       (fn [request]
         (prn request)
         (str request))))
