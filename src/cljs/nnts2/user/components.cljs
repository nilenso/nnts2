(ns nnts2.user.components
  (:require [nnts2.user.api :as api]))

(enable-console-print!)

(defn login-panel [user-info]
  (js/console.log "login is being called")
  [:div.topnav
   user-info])
