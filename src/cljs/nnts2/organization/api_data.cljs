(ns nnts2.organization.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

(defn create-org
  [details]
  {:method :post
   :response-format (ajax/json-response-format {:keywords? true})
   :format (ajax/json-request-format)
   :params details
   :uri "/org"
   :on-success [:nnts2.organization.events/organization-created]})

(def get-org
  {:method :get
   :response-format (ajax/json-response-format {:keywords? true})
   :format (ajax/json-request-format)
   :uri "/org"
   :on-success [:nnts2.organization.events/organizations-retrieved]})
