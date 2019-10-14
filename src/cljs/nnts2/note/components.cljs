(ns nnts2.note.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [nnts2.note.subs :as subs]))

(enable-console-print!)

(defn write-note []
  (let [note-form    (re-frame/subscribe [::subs/note-form])
        selected-dir (re-frame/subscribe [:nnts2.directory.subs/selected-directory])]
    (fn []
      [:form
       [:fieldset
        [:label {:for "title-field"} "Title"]
        [:textarea
         {:value       (:title @note-form)
          :id          "title-field"
          :style       {:margin 0}
          :placeholder "What are you thinking of"
          :on-change   #(re-frame/dispatch
                         [:nnts2.note.events/note-form-changed :title (-> % .-target .-value)])}]
        [:label {:for "content-field"} "Content"]
        [:textarea
         {:id          "content-field"
          :style       {:height "200px"}
          :placeholder "Just start typing"
          :value       (:content @note-form)
          :on-change   #(re-frame/dispatch
                         [:nnts2.note.events/note-form-changed :content (-> % .-target .-value)])}]
        [:button {:type     "submit"
                  :disabled (if @selected-dir false true)
                  :value    "Save"
                  :on-click (fn [e]
                              (.preventDefault e)
                              (re-frame/dispatch [:nnts2.note.events/note-submit @note-form @selected-dir]))}
         "Save"]]])))

(defn note [note-data]
  [:pre [:code
         [:label {:for (str (:id note-data) "content")} (:title note-data)]
         [:div {:key (str (:id note-data) "content")} (:content note-data)]]])

(defn list-notes [notes]
  [:div {:style {:overflow-y    "auto"
                 :height        "300px"
                 :margin-bottom "1em"}}
   (for [k notes] ^{:key (:id k)} [note k])])

(defn note-panel []
  (let [notes (re-frame/subscribe [::subs/notes])]
    [:div
     [write-note]
     [list-notes @notes]]))
