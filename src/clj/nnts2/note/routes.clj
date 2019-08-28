(ns nnts2.note.routes
  (:require [compojure.core :refer [GET POST defroutes]]))

(defroutes routes
  (POST "/create" [] "create"))
