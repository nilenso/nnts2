(ns nnts2.routes.note
  (:require [compojure.api.sweet :refer [context GET POST defroutes]]
            [nnts2.handler.note :as handler]
            [clojure.spec.alpha :as s]))

(s/def ::uuid uuid?)

(defroutes routes
  (context "/dir/:dir-id/note" []
    :coercion :spec
    :path-params [dir-id :- ::uuid]
    (GET "/" []
      #(handler/get-notes % dir-id))
    (POST "/" []
      #(handler/create % dir-id))))
