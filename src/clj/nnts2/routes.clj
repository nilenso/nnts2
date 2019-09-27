(ns nnts2.routes
  (:require [nnts2.routes.user :as user]
            [nnts2.routes.note :as note]
            [nnts2.routes.organization :as organization]
            [nnts2.routes.directory :as directory]
            [compojure.api.sweet :refer [api]]))


(def auth-routes
  (api
   user/routes
   organization/routes
   note/routes
   directory/dir-routes))
