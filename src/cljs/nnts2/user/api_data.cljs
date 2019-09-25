(ns nnts2.user.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))

(enable-console-print!)

                                        ; TODO: handle on-failure.
(defn user-info []
  {:method          :get
   :uri             "/user-info"
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      [:nnts2.user.events/user-info-retrieved]})
