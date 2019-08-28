(ns nnts2.organization.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

(defn create [org-details]
  (prn "ORG DETAILS " org-details)
  (POST "/create-organization"
       {:params          org-details
        :response-format :json
        :keywords?       true
        :format          :json
        :handler         #(prn "Org created! " %)}))
