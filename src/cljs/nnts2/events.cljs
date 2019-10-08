(ns nnts2.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.db :as db]
   [nnts2.note.events :as note-events]
   [nnts2.organization.events :as org-events]
   [nnts2.user.events :as user-events]
   [nnts2.directory.events :as dir-events]
   [nnts2.user.api-data :as user-api-data]
   [nnts2.note.api-data :as note-api-data]
   [nnts2.organization.api-data :as org-api-data]))

(enable-console-print!)

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-fx
 ::navigated-to-home
 (fn [cofx _]
   {:db (assoc (:db cofx) :active-panel :home-panel)
    :http-xhrio [(user-api-data/user-info)
                 (note-api-data/get-notes)
                 (org-api-data/get-org)]}))
