(ns nnts2.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [nnts2.events :as events]
   [nnts2.routes :as routes]
   [nnts2.views :as views]
   [nnts2.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/init)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
