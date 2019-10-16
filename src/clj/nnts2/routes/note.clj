(ns nnts2.routes.note
  (:require [compojure.api.sweet :refer [context GET POST defroutes]]
            [nnts2.handler.note :as handler]
            [nnts2.spec.note :as spec]))

(defroutes routes
  (context "/dirs/:dir-id/notes" []
    :coercion :spec
    :path-params [dir-id :- ::spec/uuid]
    (GET "/" []
      #(handler/get-notes % dir-id))
    (POST "/" []
      :body [body ::spec/note]
      #(handler/create % dir-id))))
