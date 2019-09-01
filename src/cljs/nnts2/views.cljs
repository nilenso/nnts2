(ns nnts2.views
  (:require
    [re-frame.core :as re-frame]
    [nnts2.subs :as subs]
    [re-frame.core :as re-frame]
    [nnts2.organization.create :as create-org]))


(defn home-panel []
  (let [user-info (re-frame/subscribe [:user])]
    (fn []
      (let [name (:given-name @user-info)]
        [:div.main
         [:h1 (str "Hello " name ". This is the Home Page.")]
         [(create-org/form)]
         [:div
          [:a {:href "#/about"}
           "go to About Page"]]]))))

(defn- org-item [{:keys [name]}]
  ^{:key name}
  [:a {:href "#"} name])

(defn side-panel []
  (let [orgs-subscription (re-frame/subscribe [:organization])]
    (fn []
      (let [orgs (first @orgs-subscription)]
        [:div.sidenav
         (map org-item orgs)]))))

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
