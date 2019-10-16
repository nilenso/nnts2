(ns nnts2.organization.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

(defn create-org
  [details]
  {:method          :post
   :response-format (ajax/json-response-format {:keywords? true})
   :format          (ajax/json-request-format)
   :params          details
   :uri             "/orgs"
   :on-success      [:nnts2.organization.events/organization-created-success]
   :on-failure      [:nnts2.organization.events/organization-created-failure]})

(defn get-org []
  {:method          :get
   :response-format (ajax/json-response-format {:keywords? true})
   :format          (ajax/json-request-format)
   :uri             "/orgs"
   :on-success      [:nnts2.organization.events/organizations-retrieved]})
