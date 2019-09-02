(ns nnts2.note.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [nnts2.note.subs :as subs]))

(enable-console-print!)

(defn write-notes []
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


(defn list-notes []
  [:div [:h1 "I will list notes here"]])


(defn note-panel []
  [:div
   ;[:div [list-notes]]
   [:div [write-notes]]])
