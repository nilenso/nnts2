(ns nnts2.routes.directory
  (:require [compojure.api.sweet :refer [context POST GET resource defroutes]]
            [nnts2.handler.directory :as directory]
            [clojure.spec.alpha :as s]))


(s/def ::uuid uuid?)
(s/def ::parent-id ::uuid)
(s/def ::org-id ::uuid)
(s/def ::show-tree boolean?)

(s/def ::directory (s/keys :opt-un [::parent-id]
                           :req-un [::name]))


(defroutes dir-routes
  (context "/org/:org-id/dir" []
    :coercion :spec
    :path-params [org-id :- ::org-id]
    (GET "/" []
      :query-params [{parent-id :- ::parent-id nil}
                     {show-tree :- ::show-tree false}]
      #(directory/list % org-id parent-id show-tree))
    (GET "/:id" []
      :path-params [id :- ::uuid]
      #(directory/find % org-id id))
    (POST "/" []
      :body [body ::directory]
      #(directory/create % org-id body))))