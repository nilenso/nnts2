(ns nnts2.views
  (:require
    [re-frame.core :as re-frame]
    [nnts2.subs :as subs]
    [re-frame.core :as re-frame]
    [nnts2.user.views :as user-views]
    [nnts2.organization.views :as org-views]))


(defn home-panel []
  (fn []
    [:div.main
     [(org-views/create-form)]
     [:div
      [:a {:href "#/about"}
       "go to About Page"]]]))

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
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
