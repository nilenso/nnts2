(ns nnts2.routes.directory
  (:require [compojure.api.sweet :refer [context POST GET resource defroutes]]
            [nnts2.handler.directory :as directory]
            [nnts2.spec.directory :as spec]))

(defroutes dir-routes
  (context "/orgs/:org-id/dirs" []
    :coercion :spec
    :path-params [org-id :- ::spec/org-id]
    (GET "/" []
      :query-params [{parent-id :- ::spec/parent-id nil}
                     {show-tree :- ::spec/show-tree false}]
      #(directory/list % org-id parent-id show-tree))
    (GET "/:id" []
      :path-params [id :- ::spec/uuid]
      #(directory/find % org-id id))
    (POST "/" []
      :body [body ::spec/directory]
      #(directory/create % org-id body))))
