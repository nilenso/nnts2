(ns nnts2.user.login-component
  (:require [nnts2.user.login-api :as api]))


(enable-console-print!)

(defn login-panel []
  (js/console.log "login is being called")
  (api/login)
  [:div [:h1 "This is the login Page."]])
