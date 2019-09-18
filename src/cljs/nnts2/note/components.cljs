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


(defn list-notes []
  [:div [:h1 "I will list notes here"]])


(defn note-panel []
  [:div
                                        ;[:div [list-notes]]
   [:div [write-notes]]])
