(ns nnts2.views
  (:require
   [re-frame.core :as re-frame]
   [nnts2.subs :as subs]
   [nnts2.user.views :as user-views]
   [nnts2.note.components :as note]
   [nnts2.organization.views :as org-views]))

(enable-console-print!)

;; home

(defn home-panel []
  (fn []
    [:div [note/note-panel]]))

(defn side-panel []
  (fn []
    [:div.sidenav
     [(user-views/greeting)]
     [(org-views/sidenav)]]))

;; about

(defn about-panel []
  [:div.main
   [:h1 "This is the About Page."]

   [:div
    [:a {:href "#/"}
     "go to Home Page"]]])

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    :note-panel [note/note-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
