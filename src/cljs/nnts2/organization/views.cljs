(ns nnts2.organization.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [nnts2.organization.subs :as subs]
            [nnts2.organization.events :as events]
            [nnts2.directory.components :as directories]))

(defn organization-view [id {:keys [name]}]
  "showing organization using directory view but with nil id, also initializing org-directories component"
  ^{:key id}
  [:ul.dir-list
   [:li.org-name
    [directories/directory {:name   name
                            :org-id id
                            :id     nil}]]
   [directories/directory-list id]])

(defn create-form []
  (let [add-org-error (re-frame/subscribe [::subs/create-org-form-failure])
        org-details   (reagent/atom {:name ""
                                     :slug ""})]
    (fn []
      (let [{:keys [name slug]} @org-details]
        [:div

         [:div.row
          [:input {:type        :text
                   :id          "org-name"
                   :style       {:color "white"}
                   :class       [(if (:error @add-org-error) :error-form)]
                   :placeholder "Org Name"
                   :on-change   (fn [e]
                                  (swap! org-details assoc :name (-> e .-target .-value)))}]]
         [:div.row
          [:input {:type        :text
                   :id          "org-slug"
                   :style       {:color "white"}
                   :class       [(if (:error @add-org-error) :error-form)]
                   :placeholder "Slug"
                   :on-change   (fn [e]
                                  (swap! org-details assoc :slug (-> e .-target .-value)))}]]
         (when (:error @add-org-error) [:label.error-msg (:message @add-org-error)])
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
          [:div {:class "column column-80"
                 :style {:color "#d5d5d5"}}
           [:h6 "Organizations"]]
          [:div {:class "column column-20"} [:a
                                             {:href     "#"
                                              :on-click #(re-frame/dispatch [::events/show-create-org-form])}
                                             "+"]]]
         (if @show-create-org-form
           [create-form]
           [:div])
         [:div
          {:style {:overflow-x "auto"}}
          [:div.explain-msg
           "*Dbl click to create sub directory"]
          (for [[k v] orgs]
            [organization-view k v])]]))))
