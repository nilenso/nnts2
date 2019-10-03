(ns nnts2.directory.events
  (:require [re-frame.core :as re-frame]
            [nnts2.directory.api-data :as api-data]))

(enable-console-print!)

(re-frame/reg-event-fx
 ::create-directory-submit
 (fn [cofx [_ dir-data]]
   (prn dir-data)
   {:db (:db cofx)
    :http-xhrio (api-data/create-directory dir-data)}))

(re-frame/reg-event-fx
 ::create-directory-success
 (fn [cofx [_ dir-data]]
   (prn "received org id" (:org-id dir-data))
   {:db (:db cofx)
    :http-xhrio (api-data/get-directories (:org-id dir-data))}))


(re-frame/reg-event-db
 ::received-directory-list
 (fn [db [_ directories]]
   (prn "directories" directories)
   (assoc db :directories directories)))
