(ns nnts2.organization.create
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

(defn form []
  (let [org-details (reagent/atom {:name ""
                                   :slug ""})]
    (prn "ORGANIZATIONS CALLED")
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
