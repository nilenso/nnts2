(ns nnts2.routes.directory
  (:require [compojure.api.sweet :refer [context POST GET resource defroutes]]
            [nnts2.handler.directory :as directory]
            [clojure.spec.alpha :as s]))


(s/def ::uuid uuid?)
(s/def ::parent-id (s/or :uuid ::uuid :nil nil?))
(s/def ::org-id ::uuid)
(s/def ::recursive boolean?)

(s/def ::directory (s/keys :opt-un [::parent-id]
                           :req-un [::name]))


(defroutes dir-routes
  (context "/org/:org-id/dir" []
    :coercion :spec
    :path-params [org-id :- ::org-id]
    (GET "/" []
      :query-params [{parent-id :- ::parent-id nil}
                     {recursive :- ::recursive false}]
      #(directory/list % org-id parent-id recursive))
    (GET "/:id" []
      :path-params [id :- ::uuid]
      #(directory/find % org-id id))
    (POST "/" []
      :body [body ::directory]
      #(directory/create % org-id body))))
