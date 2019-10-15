(ns nnts2.organization.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [nnts2.organization.subs :as subs]
            [nnts2.organization.events :as events]
            [nnts2.directory.components :as directories]))

(defn organization-view [id {:keys [name]}]
  "showing organization using directory view but with nil id, also initializing org-directories component"
  ^{:key id}
  [:ul  [:b [directories/directory {:name   name
                                    :org-id id
                                    :id     nil}]]
   [directories/directory-list id]])

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

(defn organization-list []
  (let [orgs-subscription    (re-frame/subscribe [::subs/organization])
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
         [:div {:style {:overflow-x "auto"}}
          (for [[k v] orgs]
            [organization-view k v])]]))))
