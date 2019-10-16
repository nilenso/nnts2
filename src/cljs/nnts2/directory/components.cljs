(ns nnts2.directory.components
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame]
            [nnts2.directory.subs :as subs]))

(defn add-child-directory [{:keys [id org-id]}]
  (let [dir-details (r/atom {:name      ""
                             :org-id    org-id
                             :parent-id id})]
    [:div {:class "row"}
     [:div
      {:class "column column-80"}
      [:input {:type      "text"
               :style     {:color         "#FFFFFF"
                           :margin-bottom 0}
               :on-change (fn [e] (swap! dir-details assoc :name (-> e .-target .-value)))}]]
     [:div
      {:class "column column-10"
       :style {:padding 0}}
      [:button
       {:style    {:padding-left  "5px"
                   :padding-right "5px"
                   :margin-bottom 0}
        :on-click #(re-frame/dispatch [:nnts2.directory.events/create-directory-submit @dir-details])}
       "→"]]]))

(defn directory [{:keys [id name org-id] :as dir}]
  (let [selected (re-frame/subscribe [::subs/is-selected-directory id])
        add-new  (re-frame/subscribe [::subs/add-sub-directory  (or id org-id)])]
    (fn []
      [:span
       {:id            id
        :onDoubleClick #(re-frame/dispatch [:nnts2.directory.events/directory-add-new-subdir (or id org-id)])
        :on-click      #(re-frame/dispatch [:nnts2.directory.events/directory-selected id])
        :style         (if @selected {:color "orange"} {})}
       name
       [:div {:style (if (not @add-new)
                      {:display "none"}
                      {:min-width "130px"
                       :margin-left "0.8rem"
                       :margin-top ".4rem"})}
        [add-child-directory dir]]])))

(defn directory-collection->directory-element [{:keys [id directories] :as dir-coll}]
  "create a hiccup element out of directory collection. A map within the collection is mapped to 'li' and a list is mapped to 'ul'"
  (if (nil? dir-coll)
    nil
    (if (map? dir-coll)
      ^{:key id} [:li [directory dir-coll] (directory-collection->directory-element directories)]
      [:ul.dir-list (map directory-collection->directory-element dir-coll)])))

(defn directory-list [org-id]
  (let [directories (re-frame/subscribe [::subs/org-directories org-id])]
    (directory-collection->directory-element @directories)))
