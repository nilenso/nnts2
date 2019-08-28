(ns nnts2.events
  (:require
    [re-frame.core :as re-frame]
    [nnts2.db :as db]
    [nnts2.user.api :as user-api]
    [nnts2.note.events :as note-events]))

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
 ::get-user-info
  (fn [_ event]
    (user-api/get-info)))


(re-frame/reg-event-db
  :user-info
  (fn [db [_ info]]
    (assoc db :user-id (:given-name info))))
