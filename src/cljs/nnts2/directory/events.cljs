(ns nnts2.directory.events
  (:require [re-frame.core :as re-frame]
            [nnts2.directory.api-data :as api-data]
            [nnts2.note.api-data :as note-api]))

(enable-console-print!)

(re-frame/reg-event-fx
 ::create-directory-submit
 (fn [cofx [_ dir-data]]
   {:db (:db cofx)
    :http-xhrio (api-data/create-directory dir-data)}))

(re-frame/reg-event-fx
 ::create-directory-success
 (fn [cofx [_ dir-data]]
   {:db (:db cofx)
    :http-xhrio (api-data/get-directories (:org-id dir-data))}))


(re-frame/reg-event-db
 ::received-directory-list
 (fn [db [_ directories]]
   (assoc-in db [:organization (:org-id (first directories)) :directories] directories )))

(re-frame/reg-event-fx
 ::get-directories
 (fn [cofx [_ org-id]]
   {:http-xhrio (api-data/get-directories org-id)}))


(re-frame/reg-event-fx
 ::directory-selected
 (fn [cofx [_ dir-id]]
   (if dir-id
     {:db (assoc (:db cofx) :selected-dir dir-id)
      :http-xhrio (note-api/get-notes dir-id)}
     {:db (assoc (:db cofx) :selected-dir dir-id)})))
