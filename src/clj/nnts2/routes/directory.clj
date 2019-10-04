(ns nnts2.routes.directory
  (:require [compojure.api.sweet :refer [context POST GET resource defroutes]]
            [nnts2.handler.directory :as directory]
            [clojure.spec.alpha :as s]))


(def alnum-regex #"[a-zA-Z0-9]+")

(s/def ::uuid uuid?)
(s/def ::name (s/and string? #(re-matches alnum-regex %)))
(s/def ::parent-id (s/or :uuid ::uuid :nil nil?))
(s/def ::org-id ::uuid)
(s/def ::show-tree boolean?)

(s/def ::directory (s/keys :opt-un [::parent-id]
                           :req-un [::name]))


(defroutes dir-routes
  (context "/orgs/:org-id/dirs" []
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
