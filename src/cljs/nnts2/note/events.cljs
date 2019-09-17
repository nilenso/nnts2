(ns nnts2.note.events
  (:require [re-frame.core :as re-frame]
            [nnts2.note.api :as api]))

(enable-console-print!)

(re-frame/reg-event-fx
 :note-submit
 (fn [_ [_ note]]
   (api/create-note note)))

(re-frame/reg-event-db
 :note-submit-success
 (fn [db event]
   (assoc db :note-form {:title "" :content ""})))

(re-frame/reg-event-db
 :note-get-list
 (fn [db event]
   db))
