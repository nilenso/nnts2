(ns nnts2.note.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [nnts2.note.subs :as subs]))

(enable-console-print!)


(defn write-note []
  [:div "empty"]
  (let [note-form (re-frame/subscribe [::subs/note-form])
        title-text (r/atom "")
        content-text (r/atom "")]
    (fn []
      [:form
       [:fieldset
        [:label {:for "title-field"} "Title"]
        [:textarea
         {:value @title-text
          :id "title-field"
          :placeholder "What are you thinking of"
          :on-change #(reset! title-text (-> % .-target .-value))}]
        [:label {:for "content-field"} "Content"]
        [:textarea
         {:id "content-field"
          :placeholder "Just start typing"
          :value @content-text
          :on-change #(reset! content-text (-> % .-target .-value))}]
        [:button {:type "submit"
                  :value "Save"
                  :on-click (fn [e]
                              (.preventDefault e)
                              (re-frame/dispatch [:note-submit {:title @title-text :content @content-text}]))}
         "Save"]]])))



(defn note [note-data]
  [:div {:id (:id note-data)
         :style {:margin-top ".5em"}}
   [:div {:style {:background-color (str "rgb(100,100,100)")
                  :padding ".5em"}
          } (:title note-data)]
   [:div {:style {:color "white"
                  :padding ".5em"
                  :background-color (str "rgb(40,44,52)")}}
    (:content note-data)]])



(defn list-notes [notes]
  [:div {:style {:overflow-y "auto"
                 :height "300px"
                 :margin-bottom "1em"}}
   (for [k notes] ^{:key (:id k)}[note k])])



(defn note-panel []
  (let [notes (re-frame/subscribe [::subs/notes])]
    [:div
     [write-note]
     [list-notes @notes]]))
