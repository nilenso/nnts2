(ns nnts2.events
  (:require
    [re-frame.core :as re-frame]
    [nnts2.db :as db]
    [nnts2.user.api :as user-api]))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  ::set-active-panel
  (fn [db [_ active-panel]]
    (prn "Active panel db" db)
    (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
  ::get-user-info
  (fn [db event]
    (user-api/get-info (second event) db)))


(re-frame/reg-event-db
  :user-info
  (fn [db [_ info]]
    (assoc db/default-db :user-id (:id info))))