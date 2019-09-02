(ns nnts2.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.db :as db]
   [nnts2.organization.api :as org-api]
   [nnts2.user.api :as user-api]))

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
 (fn [{db :db} event]
   {:http-xhrio user-api/user-info-map
    :db (assoc-in db [:user :event :get :state] :loading)}))

(re-frame/reg-event-fx
 :create-organization
 (fn [{db :db}  [_ org-details]]
   {:http-xhrio (org-api/create-map org-details)
    :db (assoc-in db [:organization :event :create :state] :loading)}))

(re-frame/reg-event-db
 :organization-created
 (fn [db [_ org-details]]
   (-> db
       (update-in [:organization :event :create :state] :created)
       (update-in [:organization :data :all] (fnil conj []) org-details))))

(re-frame/reg-event-db
 :user-info-retrieved
 (fn [db [_ user-info]]
   (re-frame/dispatch [:get-all-organizations])
   (-> db
       (update-in [:user :event :get :state] :retrieved)
       (assoc-in [:user :data] user-info))))

(re-frame/reg-event-fx
 :get-all-organizations
 (fn [{db :db} _]
   {:http-xhrio org-api/get-all-map
    :db (assoc-in db [:organization :event :get :state] :loading)}))

(re-frame/reg-event-db
 :organizations-retrieved
 (fn [db [_ org-details]]
   (-> db
       (update-in [:organization :event :get :state] :retrieved)
       (assoc-in [:organization :data :all] org-details))))