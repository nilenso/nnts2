(ns nnts2.user.components
  (:require [nnts2.user.api :as api]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn header []
  (let [header-panel (re-frame/subscribe [:header-panel])]
    (prn "login is being called " @header-panel)
    [:div.topnav
     (first (vals @header-panel))]))
