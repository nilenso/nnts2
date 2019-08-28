(ns nnts2.note.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]))

(enable-console-print!)

(def full (r/atom ""))

(defn write-notes []
  (let [content-text (r/atom "")
        title-text (r/atom "")]
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
   [:div [write-notes]]
   [:div @full]])
