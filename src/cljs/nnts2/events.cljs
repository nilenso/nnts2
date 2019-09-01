(ns nnts2.events
  (:require
    [re-frame.core :as re-frame]
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
  (fn [_ event]
    (user-api/get-info)))

(re-frame/reg-event-db
  :create-organization
  (fn [db [_ org-details]]
    (org-api/create org-details)
    (assoc-in db [:create-organization :state] :loading)))

(re-frame/reg-event-db
  :organization-created
  (fn [db [_ org-details]]
    (assoc db :organization-list org-details)))

(re-frame/reg-event-db
  :user-info
  (fn [db [_ info]]
    (assoc db :user info)))

(re-frame/reg-event-fx
 :get-orgs
 (fn [coeffects [_ data]]
   (org-api/get-all)))
