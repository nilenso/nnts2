(ns nnts2.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [nnts2.config :refer [server-spec]]
            [compojure.core :refer [GET defroutes ANY context]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response redirect]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.oauth2 :refer [wrap-oauth2]]
            [nnts2.middleware :refer [wrap-kebab-case not-found wrap-exception-handling wrap-log-request-response wrap-validate-access-token]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.json :refer [wrap-json-response]]
            [nnts2.config :refer [oauth2-spec]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.resource :as resource]
            [ring.middleware.session.memory :as mem]
            [nnts2.user.routes :as user]
            [nnts2.note.routes :as note]
            [ring.middleware.cookies :as cookies]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]
            [ring.util.response :refer [file-response resource-response
                                        status content-type]]))

(defonce ^:private all-sessions (mem/memory-store))

(defroutes authenticated-routes
  (context "/note" [] note/routes)
  user/routes)

(defroutes app-routes
  (ANY "*" [] (-> authenticated-routes
                  wrap-validate-access-token
                  (wrap-oauth2 (oauth2-spec))))
  (ANY "*" [] (not-found (io/resource "public/index.html"))))

(defn handler []
  (-> app-routes
      wrap-kebab-case
      wrap-keyword-params
      wrap-exception-handling
      wrap-log-request-response
      cookies/wrap-cookies
      wrap-params
      (wrap-defaults (-> site-defaults (assoc-in [:session :cookie-attrs :same-site] :lax)))
      wrap-json-response
      (resource/wrap-resource "public")
      (wrap-session {:store all-sessions})))

(defn start []
  (let [port (Integer/parseInt (:port (server-spec)))
        host (:ip (server-spec))]
    (run-jetty (handler) {:port  port
                          :host  host
                          :join? false})))
