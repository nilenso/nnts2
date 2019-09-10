(ns nnts2.organization.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.user.events]
   [nnts2.organization.api :as org-api]))

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