(ns nnts2.note.events
  (:require [re-frame.core :as re-frame]
            [nnts2.note.api :as api]))


(enable-console-print!)


(re-frame/reg-event-fx
 :note-submit
 (fn [_ event]
   {:http-xhrio (api/create-note-req-map (first (rest event)))}))


(re-frame/reg-event-fx
 :note-submit-success
 (fn [cofx event]
   {:db  (assoc (:db cofx) :note-form {:title "" :content ""})
    :dispatch [:note-get-list]}))


(re-frame/reg-event-fx
 :note-get-list
 (fn [db event]
   {:http-xhrio (api/get-notes-req-map)}))

(re-frame/reg-event-db
 :note-received-list
 (fn [db event]
   (assoc db :notes (first (rest event)))))
