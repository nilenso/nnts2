(ns nnts2.note.events
  (:require [re-frame.core :as re-frame]
            [nnts2.note.api :as api]))


(enable-console-print!)

(re-frame/reg-event-fx
 :note-submit
 (fn [_ event]
   (api/create-note (rest event))))

(re-frame/reg-event-db
 :note-submit-success
 (fn [db event]
   (assoc db :note-form {:title "" :content ""})))

(re-frame/reg-event-fx
 :note-get-list
 (fn [_ event]
   (api/get-notes)))

(re-frame/reg-event-db
 :note-received-list
 (fn [db event]
   (assoc db :notes (first (rest event)))))
