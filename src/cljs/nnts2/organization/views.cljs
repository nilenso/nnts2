(ns nnts2.organization.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

(defn sidenav-item [{:keys [name]}]
  ^{:key name}
  [:a {:href "#"} name])

(defn sidenav []
  (let [orgs-subscription (re-frame/subscribe [:organization])]
    (fn []
      (let [orgs @orgs-subscription]
        [:div
         [:h3 "Organizations"]
         (map sidenav-item orgs)]))))

(defn create-form []
  (let [org-details (reagent/atom {:name ""
                                   :slug ""})]
    (fn []
      (let [{:keys [name slug]} @org-details]
        [:div
         [:div.row
          [:input {:type        :text
                   :value       name
                   :placeholder "Name"
                   :on-change   (fn [e]
                                  (swap! org-details assoc :name (-> e .-target .-value)))}]]
         [:div.row
          [:input {:type        :text
                   :value       slug
                   :placeholder "Slug"
                   :on-change   (fn [e]
                                  (swap! org-details assoc :slug (-> e .-target .-value)))}]]
         [:button
          {:on-click (fn [_event]
                       (re-frame/dispatch [:create-organization @org-details]))} "Create"]]))))
