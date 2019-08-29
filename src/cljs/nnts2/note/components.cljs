(ns nnts2.note.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]))

(enable-console-print!)


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


#_(defn note [note-data]
  [:div
   [:h3 (:title note-data)]
   [:h4 (:content note-data)]])

#_(defn list-notes [note-list]
  [:div
   (for [k note-list] [note k])])


(defn note-panel []
  [:div

   #_[:div [list-notes [{:title "title" :content "dfa fadfa"}
                      {:title "titl2e" :content "dfa 2fadfa"}
                      {:title "title3" :content "dfa3 fadfa"}]]]
   [:div [write-notes]]])
