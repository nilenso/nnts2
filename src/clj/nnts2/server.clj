(ns nnts2.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [GET defroutes ANY context]]
            [compojure.route :refer [resources]]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]
            [ring.util.response :refer [resource-response redirect file-response status content-type]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.oauth2 :refer [wrap-oauth2]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.json :refer [wrap-json-response  wrap-json-body]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.resource :as resource]
            [ring.middleware.session.memory :as mem]
            [ring.middleware.cookies :as cookies]
            [nnts2.http-middleware :refer [wrap-kebab-case not-found wrap-exception-handling wrap-log-request-response wrap-validate-access-token wrap-nnts-user-id]]
            [nnts2.config :refer [server-spec oauth2-spec]]
            [nnts2.routes.user :as user-routes]
            [nnts2.routes.note :as note-routes]))


(defonce ^:private all-sessions (mem/memory-store))

(def auth-routes (compojure.core/routes
                   user-routes/routes
                   (context "/note" [] note-routes/routes)))

(defroutes app-routes
  (ANY "*" [] (-> auth-routes
                  wrap-nnts-user-id
                  wrap-validate-access-token
                  (wrap-oauth2 (oauth2-spec))))
  (ANY "*" [] (not-found (io/resource "public/index.html"))))


(defn handler []
  (-> app-routes
      wrap-exception-handling
      wrap-kebab-case
      wrap-json-response
      wrap-params
      (wrap-json-body {:keywords? true})
      wrap-log-request-response
      (resource/wrap-resource "public")
      (wrap-session {:store all-sessions})))

(defn start []
  (let [port (Integer/parseInt (:port (server-spec)))
        host (:ip (server-spec))]
    (run-jetty (handler) {:port  port
                          :host  host
                          :join? false})))
