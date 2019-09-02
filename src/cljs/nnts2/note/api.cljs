(ns nnts2.note.api
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn create-note-req-map [data]
  {:method :post
   :uri "/note"
   :response-format (ajax/json-response-format {:keywords? true})
   :params data
   :format (ajax/json-request-format)
   :on-success [:note-submit-success]})

(defn get-notes-req-map []
  {:method :get
   :uri "/note"
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success [:note-received-list]})
