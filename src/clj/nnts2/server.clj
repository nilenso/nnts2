(ns nnts2.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [nnts2.config :refer [server-spec oauth2-spec]]
            [compojure.core :refer [GET defroutes ANY]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response redirect]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.keyword-params :refer [keyword-params-request]]
            [ring.middleware.oauth2 :refer [wrap-oauth2]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [nnts2.middleware :refer [wrap-kebab-case not-found wrap-exception-handling
                                      wrap-log-request-response wrap-validate-access-token]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.resource :as resource]
            [ring.middleware.session.memory :as mem]
            [nnts2.user.routes :as user]
            [nnts2.organization.routes :as organization]
            [nnts2.user.middleware :as user-middleware]
            [ring.middleware.cookies :as cookies]
            [compojure.response :refer [render]]
            [clojure.java.io :as io]
            [ring.util.response :refer [file-response resource-response
                                        status content-type]]))

(defonce ^:private all-sessions (mem/memory-store))
(defonce server (atom nil))

(def auth-routes (compojure.core/routes
                  user/routes
                  organization/routes))

(defroutes app-routes
  (ANY "*" [] (-> auth-routes
                  user-middleware/wrap-nnts-user-id
                  wrap-validate-access-token
                  (wrap-oauth2 (oauth2-spec))))
  (ANY "*" [] (not-found (io/resource "public/index.html"))))

(defn handler []
  (-> app-routes
      wrap-exception-handling
      wrap-log-request-response
      wrap-kebab-case
      wrap-json-response
      wrap-params
      (wrap-json-body {:keywords? true})
      (resource/wrap-resource "public")
      (wrap-session {:store all-sessions})))

(defn stop []
  (.stop @server)
  (reset! server nil))

(defn start []
  (let [port (Integer/parseInt (:port (server-spec)))
        host (:ip (server-spec))]
    (reset! server
            (run-jetty (handler) {:port  port
                                  :host  host
                                  :join? false}))))
