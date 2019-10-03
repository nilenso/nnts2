(ns nnts2.note.events
  (:require [re-frame.core :as re-frame]
            [nnts2.note.api-data :as api-data]))


(enable-console-print!)

(re-frame/reg-event-db
 ::note-form-changed
 (fn [db [_ key value]]
   (assoc-in db [:note-form key] value)))

(re-frame/reg-event-fx
 ::note-submit
 (fn [cofx [_ note dir]]
   {:db (:db cofx)
    :http-xhrio (api-data/create-note note dir)}))

(re-frame/reg-event-fx
 ::note-submit-success
 (fn [cofx [_ note]]
   {:db  (assoc (:db cofx) :note-form {:title "" :content ""})
    :http-xhrio (api-data/get-notes (:directory-id note))}))

(re-frame/reg-event-db
 ::note-received-list
 (fn [db [_ notes]]
   (assoc db :notes notes)))
