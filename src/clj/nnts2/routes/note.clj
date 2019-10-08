(ns nnts2.routes.note
  (:require [compojure.api.sweet :refer [context GET POST defroutes]]
            [nnts2.handler.note :as handler]
            [clojure.spec.alpha :as s]))

(s/def ::non-empty-string (s/and string? #(not (empty? %))))
(s/def ::uuid uuid?)

(s/def ::title ::non-empty-string)
(s/def ::content ::non-empty-string)
(s/def ::directory-id ::uuid)

(s/def ::note (s/keys :req-un [::title ::content]))

(defroutes routes
  (context "/dirs/:dir-id/notes" []
    :coercion :spec
    :path-params [dir-id :- ::uuid]
    (GET "/" []
      #(handler/get-notes % dir-id))
    (POST "/" []
      :body [body ::note]
      #(handler/create % dir-id))))
