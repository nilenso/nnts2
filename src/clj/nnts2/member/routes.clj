(ns nnts2.member.routes
  (:require [compojure.core :refer [defroutes GET]]
            [nnts2.member.handler :as handler]))

(defroutes routes
  (GET "/user/orgs" [] handler/get-orgs))
