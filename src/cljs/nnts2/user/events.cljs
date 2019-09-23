(ns nnts2.user.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.user.api-data :as api-data]))

(re-frame/reg-event-fx
 ::get-user-info
 (fn [{db :db} event]
   {:http-xhrio api-data/user-info
    :db (assoc-in db [:user :event :get :state] :loading)}))

(re-frame/reg-event-db
 ::user-info-retrieved
 (fn [db [_ user-info]]
   (re-frame/dispatch [:nnts2.organization.events/get-all-organizations])
   (-> db
       (update-in [:user :event :get :state] :retrieved)
       (assoc-in [:user :data] user-info))))
