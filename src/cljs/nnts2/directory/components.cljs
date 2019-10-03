(ns nnts2.directory.components
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame]
            [nnts2.directory.subs :as subs]))


(defn add-child-directory [dir]
  (let [dir-details (r/atom {:name ""
                             :org-id (:org-id dir)
                             :parent-id (:id dir)})]
    [:div {:class "row"}
     [:div
      {:class "column column-80"}
      [:input {:type "text"
               :style {:color "#FFFFFF"
                       :margin-bottom 0}
               :on-change (fn [e] (swap! dir-details assoc :name (-> e .-target .-value)))}]]
     [:div
      {:class "column column-10"
       :style {:padding 0}}
      [:button
       {:style {:padding-left "5px"
                :padding-right "5px"
                :margin-bottom 0}
        :on-click #(re-frame/dispatch [:nnts2.directory.events/create-directory-submit @dir-details])}
       "→"]]]))

(defn directory [dir]
  (let [selected-dir (re-frame/subscribe [::subs/selected-directory])
        add-new (r/atom false)]
    (fn []
      [:text
       {:id (:id dir)
        :onDoubleClick (fn [e] (swap! add-new not))
        :on-click #(re-frame/dispatch [:nnts2.directory.events/directory-selected (:id dir)])
        :style (if (:id dir) (if (= (:id dir) @selected-dir) {:color "orange"} {}) {})}
       (:name dir)
       [:dl  [:dt (if (not@add-new) {:style  {:display "none"}}) [add-child-directory dir]]]])))


(defn directory-json->directory-element [x]
  (if (nil? x)
    nil
    (if (map? x)
      ^{:key (:id x)} [:li [directory x] (directory-json->directory-element (:directories x))]
      [:ul (map directory-json->directory-element x)])))


(defn directory-list [org-id]
  (let [directories (re-frame/subscribe [::subs/org-directories org-id])]
    (directory-json->directory-element @directories)))
