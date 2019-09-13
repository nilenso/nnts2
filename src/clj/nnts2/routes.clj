(ns nnts2.routes
  (:require [nnts2.routes.user :as user]
            [nnts2.routes.note :as note]
            [nnts2.routes.organization :as organization]
            [nnts2.routes.directory :as directory]
            [compojure.core :as compojure]))

(def auth-routes (compojure.core/routes
                   user/routes
                   (compojure/context "/org" [] organization/routes)
                   (compojure/context "/note" [] note/routes)
                   (compojure/context "/org/:org-id/dir" [org-id] directory/routes)))
