(ns nnts2.note.components
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]))

(enable-console-print!)
(def full (r/atom ""))

(defn write-notes []
  (let [content-text* (r/atom "")
        title-text* (r/atom "")]
    (fn []
      [:div
       [:form
        {:on-submit #(re-frame/dispatch [:note-submit {:title @title-text* :content @content-text*}])}
        [:div [:textarea {:rows 1 :cols 100
                          :value @title-text*
                          :placeholder "Note Title"
                          :on-change #(reset! title-text* (-> % .-target .-value))}]
         [:textarea {:rows 20 :cols 100
                     :value @content-text*
                     :on-change #(reset! content-text* (-> % .-target .-value))
                     }]]
        [:button {:type "submit"} "Create Note"]]
       ])))


(defn list-notes []
  [:div [:h1 "I will list notes here"]])

;;notes
(defn note-panel []
  (prn "trying to get note panel")
  [:div
   ;[:div [list-notes]]
   [:div [write-notes]]
   [:div @full]])
