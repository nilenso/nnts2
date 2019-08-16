(ns nnts2.handler
  (:require
    [compojure.core :refer [GET defroutes]]
    [compojure.route :refer [resources]]
    [ring.util.response :refer [resource-response]]
    [ring.middleware.reload :refer [wrap-reload]]
    [shadow.http.push-state :as push-state]
    [ring.middleware.oauth2 :refer [wrap-oauth2]]
    [nnts2.config :refer [oauth2-spec]]
    [ring.middleware.defaults :refer :all]))

;(defroutes routes
;  (GET "/" [] (resource-response "index.html" {:root "public"}))
;  (resources "/"))

(defroutes routes
           (GET "/" [] "NOTHING"))

(def handler
  (fn []
    (-> routes
        (wrap-oauth2 (oauth2-spec))
        (wrap-defaults (-> site-defaults (assoc-in [:session :cookie-attrs :same-site] :lax))))))

;(def dev-handler (-> #'routes wrap-reload push-state/handle))

;(def handler routes)
