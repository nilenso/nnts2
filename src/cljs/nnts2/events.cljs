(ns nnts2.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.db :as db]
   [nnts2.note.events :as note-events]
   [nnts2.organization.events :as org-events]
   [nnts2.user.events :as user-events]))


(enable-console-print!)

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))
