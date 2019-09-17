(ns nnts2.user.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.user.api :as user-api]))

(re-frame/reg-event-fx
 ::get-user-info
 (fn [{db :db} event]
   {:http-xhrio user-api/user-info-map
    :db (assoc-in db [:user :event :get :state] :loading)}))

(re-frame/reg-event-db
 ::user-info-retrieved
 (fn [db [_ user-info]]
   (re-frame/dispatch [:nnts2.organization.events/get-all-organizations])
   (-> db
       (update-in [:user :event :get :state] :retrieved)
       (assoc-in [:user :data] user-info))))
