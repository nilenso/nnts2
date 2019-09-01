(ns nnts2.user.api
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

; TODO: handle on-failure.
(def user-info-map
  {:method          :get
   :uri             "/user-info"
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      [:user-info]})
