(ns nnts2.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
    [pushy.core :as pushy]
    [secretary.core :as secretary]
    [goog.events :as gevents]
    [goog.history.EventType :as EventType]
    [re-frame.core :as re-frame]
    [nnts2.events :as events]))

(enable-console-print!)

(defroute "/home" []
  (do
    (re-frame/dispatch [::events/set-active-panel :home-panel])
    (re-frame/dispatch [:nnts2.user.events/get-user-info])))

(defroute "/note" []
  (do
    (re-frame/dispatch [::events/set-active-panel :note-panel])))

(defroute "/about" []
          (re-frame/dispatch [::events/set-active-panel :about-panel]))

(def history (pushy/pushy secretary/dispatch!
                          (fn [route]
                            (secretary/locate-route route) route)))

(defn init []
  (pushy/start! history))
