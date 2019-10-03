(ns nnts2.directory.components
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame]
            [clojure.walk :refer [prewalk]]))

(def data [{:id 1 :name "one" :directories [{:id 2 :name "two"} {:id 3 :name "three" :directories [{:id 4 :name "four"}]}]} {:id 5 :name "five"}])

(defn add-child-directory [dir]
  (let [dir-details (r/atom {:name ""
                             :org-id "e8c2af2f-2660-4fe0-a9b8-ee63ecefba97";(:org-id dir)
                             :parent-id "d57f88c5-d109-4feb-b5c7-7f0e43510f1e";(:id dir)
                             })]
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
        :on-click #(do (prn @dir-details) (re-frame/dispatch [:nnts2.directory.events/create-directory-submit @dir-details]))}
       "→"]]
     ])
  )

(defn directory [dir]
  (let [add-new (r/atom false)]
    (fn []
      [:text
       {:onDoubleClick (fn [e] (swap! add-new not) (prn "dbl clicked" @add-new))}
       (:name dir)
       [:dl  [:dt (if (not@add-new) {:style  {:display "none"}}) [add-child-directory dir]]]])))


(defn directory-json->directory-element [x]
  (if (nil? x)
    nil
    (if (map? x)
      [:li [directory x] (directory-json->directory-element (:directories x))]
      [:ul (map directory-json->directory-element x)])))


(defn directory-list [dir-json]
  (directory-json->directory-element data))
