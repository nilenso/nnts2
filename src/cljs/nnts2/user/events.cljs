(ns nnts2.user.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.user.api-data :as api-data]))

(re-frame/reg-event-db
 ::user-info-retrieved
 (fn [db [_ user-info]]
   (-> db
       (update-in [:user :event :get :state] :retrieved)
       (assoc-in [:user :data] user-info))))
