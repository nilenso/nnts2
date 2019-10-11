(ns nnts2.note.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn create-note [data note-directory]
  {:method          :post
   :uri             (str "/dirs/" note-directory "/notes")
   :response-format (ajax/json-response-format {:keywords? true})
   :params          data
   :format          (ajax/json-request-format)
   :on-success      [:nnts2.note.events/note-submit-success]})

(defn get-notes [note-directory]
  {:method          :get
   :uri             (str "/dirs/" note-directory "/notes" )
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      [:nnts2.note.events/note-received-list]})
