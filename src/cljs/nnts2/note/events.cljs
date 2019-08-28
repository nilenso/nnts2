(ns nnts2.note.events
  (:require [re-frame.core :as re-frame]
            [nnts2.note.api :as api]))



(enable-console-print!)

(re-frame/reg-event-fx
 :note-submit
 (fn [_ event]
   (prn "received note submit" (rest event))
   (api/create-note (rest event))))

(re-frame/reg-event-db
 :note-get-list
 (fn [db event]
   db))
