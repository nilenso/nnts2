(ns nnts2.note.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [nnts2.note.subs :as subs]))

(enable-console-print!)


(defn write-note []
  [:div "empty"]
  (let [note-form (re-frame/subscribe [::subs/note-form])
        title-text (r/atom (:title @note-form))
        content-text (r/atom (:content @note-form))]
    (fn []
      [:div
       [:div
        {:class "Row"}
        [:textarea
         {:rows 1 :cols 100
          :value @title-text
          :placeholder "Note Title"
          :on-change #(reset! title-text (-> % .-target .-value))}]]
       [:div
        {:class "Row"}
        [:textarea
         {:align "left"
          :rows 20 :cols 100
          :value @content-text
          :on-change #(reset! content-text (-> % .-target .-value))
          }]]
       [:button
        {:type "submit"
         :align "left"
         :on-click (fn [e]
                     (.preventDefault e)
                     (re-frame/dispatch [:note-submit {:title @title-text :content @content-text}]))}
        "Create Note"]
       ])))



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
    [:div {:style {:max-width "620px"}}
     [write-note]
     [list-notes @notes]]))
