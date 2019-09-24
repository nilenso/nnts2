(ns nnts2.organization.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [nnts2.organization.subs :as subs]
            [nnts2.organization.events :as events]))

(defn sidenav-item [{:keys [name]}]
  ^{:key name}
  [:a {:href "#"} name])

(defn create-form []
  (let [org-details (reagent/atom {:name ""
                                   :slug ""})]
    (fn []
      (let [{:keys [name slug]} @org-details]
        [:div

         [:div.row
          [:input {:type        :text
                   :id          "org-name"
                   :style       {:color "white"}
                   :placeholder "Org Name"
                   :on-change   (fn [e]
                                  (swap! org-details assoc :name (-> e .-target .-value)))}]]
         [:div.row
          [:input {:type        :text
                   :id          "org-slug"
                   :style       {:color "white"}
                   :placeholder "Slug"
                   :on-change   (fn [e]
                                  (swap! org-details assoc :slug (-> e .-target .-value)))}]]
         [:button
          {:on-click (fn [_event]
                       (re-frame/dispatch [::events/create-organization @org-details]))} "Create"]]))))



(defn sidenav []
  (let [orgs-subscription (re-frame/subscribe [::subs/organization])
        show-create-org-form (re-frame/subscribe [::subs/show-create-org-form])]
    (fn []
      (let [orgs @orgs-subscription]
        [:div
         {:class "container"}
         [:div {:class "row"}
          [:div {:class "column column-80"} [:h6 "Organizations"]]
          [:div {:class "column column-20"} [:button-clear
                                             {:on-click #(re-frame/dispatch [::events/show-create-org-form])}
                                             "+"]]]
         (if @show-create-org-form
           [create-form]
           [:div])
         (map sidenav-item orgs)]))))
