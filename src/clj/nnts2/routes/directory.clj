(ns nnts2.routes.directory
  (:require [compojure.core :refer [defroutes GET]]
            [nnts2.handler.directory :as directory]))


;;so the org, directory and note routes are really interlinked. how to separate them.

(defroutes routes
  (GET "/" [] directory/get)
  (GET "/:dir-id" [dir-id] directory/get))
