(ns nnts2.note.routes
  (:require [compojure.core :refer [GET POST defroutes]]
            [nnts2.note.handler :as api]))

(defroutes routes
    (POST "/create" []
       api/create))
