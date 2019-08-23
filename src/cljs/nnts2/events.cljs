(ns nnts2.events
  (:require
    [re-frame.core :as re-frame]
    [nnts2.db :as db]
    [nnts2.user.api :as user-api]))

(re-frame/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  :set-active-panel
  (fn [db [_ active-panel]]
    (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
  :get-user-info
  (fn [db event]
    (user-api/get-info (second event) db)
    (assoc-in db [:state :loading] true)))

(re-frame/reg-event-db
  :user-info-retrieved
  (fn [db [_ user-info]]
    (-> db
        (assoc-in [:state :loading] true)
        (assoc :user-info user-info))))
