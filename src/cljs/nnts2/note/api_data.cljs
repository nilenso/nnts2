(ns nnts2.note.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn create-note [data]
  {:method          :post
   :uri             "/notes"
   :response-format (ajax/json-response-format {:keywords? true})
   :params          data
   :format          (ajax/json-request-format)
   :on-success      [:nnts2.note.events/note-submit-success]})

(defn get-notes []
  {:method          :get
   :uri             "/notes"
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      [:nnts2.note.events/note-received-list]})
