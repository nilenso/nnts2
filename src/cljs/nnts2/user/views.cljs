(ns nnts2.user.views
  (:require [re-frame.core :as re-frame]))

(defn greeting []
  (let [user-info (re-frame/subscribe [:user])]
    (fn []
      (let [name (:given-name @user-info)]
        [:h1 (str "Hello " name ". This is the Home Page.")]))))