(ns nnts2.routes.note
  (:require [compojure.api.sweet :refer [context GET POST defroutes]]
            [nnts2.handler.note :as handler]
            [clojure.spec.alpha :as s]))

(defroutes routes
  (context "/notes" []
    (GET "/" [] handler/get-notes)
    (POST "/" [] handler/create)))
