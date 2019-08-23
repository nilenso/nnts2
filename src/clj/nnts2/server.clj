(ns nnts2.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [nnts2.config :refer [server-spec]]
            [compojure.core :refer [GET defroutes ANY]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response redirect]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.oauth2 :refer [wrap-oauth2]]
            [nnts2.middleware :refer [wrap-kebab-case]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.json :refer [wrap-json-response]]
            [nnts2.config :refer [oauth2-spec]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.logger :as logger]
            [ring.middleware.resource :as resource]
            [ring.middleware.session.memory :as mem]
            [nnts2.user.routes :as user]
            [ring.util.response :as resp]
            [ring.middleware.cookies :as cookies]))


(defonce ^:private all-sessions (mem/memory-store))

(defroutes routes
           (GET "/" [] (redirect "/index.html")))

(defroutes app-routes
           (ANY "*" [] routes)
           (ANY "*" [] (-> user/routes
                           (wrap-oauth2 (oauth2-spec)))))

(defn handler []
  (-> app-routes
      wrap-kebab-case
      wrap-keyword-params
      cookies/wrap-cookies
      wrap-params
      (wrap-defaults (-> site-defaults (assoc-in [:session :cookie-attrs :same-site] :lax)))
      wrap-json-response
      (logger/wrap-with-logger)
      (resource/wrap-resource "/public")
      (wrap-session {:store all-sessions})))

(defn start []
  (let [port (Integer/parseInt (:port (server-spec)))
        host (:ip (server-spec))]
    (run-jetty (handler) {:port  port
                          :host  host
                          :join? false})))
