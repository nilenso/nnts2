(ns nnts2.routes.directory
  (:require [compojure.core :refer [defroutes GET POST]]
            [nnts2.handler.directory :as directory]))


;;so the org, directory and note routes are really interlinked. how to separate them.
(defroutes routes
  (GET "/" [] directory/get)
  (GET "/:id" [id] directory/get)
  (POST "/" [] directory/create))
