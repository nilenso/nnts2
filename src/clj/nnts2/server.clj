(ns nnts2.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [nnts2.config :refer [server-spec]]
            [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.oauth2 :refer [wrap-oauth2]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.json :refer [wrap-json-response]]
            [nnts2.config :refer [oauth2-spec]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.logger :as logger]
            [ring.middleware.session.memory :as mem]
            [nnts2.user.routes :as user]))


(defonce ^:private all-sessions (mem/memory-store))

(defn handler []
  (-> user/routes
      (wrap-oauth2 (oauth2-spec))
      wrap-keyword-params
      wrap-params
      (wrap-defaults (-> site-defaults (assoc-in [:session :cookie-attrs :same-site] :lax)))
      wrap-json-response
      (logger/wrap-with-logger)
      (wrap-session {:store all-sessions})))

(defn start []
  (let [port (Integer/parseInt (:port (server-spec)))
        host (:ip (server-spec))]
    (run-jetty (handler) {:port  port
                          :host  host
                          :join? false})))
