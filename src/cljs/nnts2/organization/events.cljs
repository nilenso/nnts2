(ns nnts2.organization.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.user.events]
   [nnts2.organization.api-data :as api-data]))

(re-frame/reg-event-fx
 ::create-organization
 (fn [{db :db}  [_ org-details]]
   {:http-xhrio (api-data/create-org org-details)
    :db (assoc-in db [:organization :event :create :state] :loading)}))

(re-frame/reg-event-db
 ::organization-created
 (fn [db [_ org-details]]
   (-> db
       (update-in [:organization :event :create :state] :created)
       (update-in [:organization :data :all] (fnil conj []) org-details)
       (assoc-in [:organization :show-create-org-form] false)
       )))

(re-frame/reg-event-fx
 ::get-all-organizations
 (fn [{db :db} _]
   {:http-xhrio api-data/get-org
    :db (assoc-in db [:organization :event :get :state] :loading)}))

(re-frame/reg-event-db
 ::organizations-retrieved
 (fn [db [_ org-details]]
   (-> db
       (update-in [:organization :event :get :state] :retrieved)
       (assoc-in [:organization :data :all] org-details))))


(re-frame/reg-event-db
 ::show-create-org-form
 (fn [db [_ show-form]]
   (assoc-in db [:organization :show-create-org-form] (not (get-in db [:organization :show-create-org-form])))))
