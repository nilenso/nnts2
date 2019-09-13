(ns nnts2.organization.api
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

(defn create-map
  [details]
  {:method :post
   :response-format (ajax/json-response-format {:keywords? true})
   :format (ajax/json-request-format)
   :params details
   :uri "/org"
   :on-success [:organization-created]})

(def get-all-map
  {:method :get
   :response-format (ajax/json-response-format {:keywords? true})
   :format (ajax/json-request-format)
   :uri "/org"
   :on-success [:organizations-retrieved]})
