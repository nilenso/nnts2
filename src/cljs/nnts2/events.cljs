(ns nnts2.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.db :as db]
   [nnts2.note.events :as note-events]
   [nnts2.organization.api :as org-api]
   [nnts2.user.api :as user-api]))


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
 (fn [{db :db} event]
   {:http-xhrio user-api/user-info-map
    :db (assoc db :loading true)}))

(re-frame/reg-event-fx
 :create-organization
 (fn [db [_ org-details]]
   {:http-xhrio (org-api/create-map org-details)
    :db (assoc-in db [:create-organization :state] :loading)}))

(re-frame/reg-event-db
 :organization-created
 (fn [db [_ org-details]]
   (assoc db :organization-list org-details)))

(re-frame/reg-event-db
 :user-info
 (fn [db [_ info]]
   (assoc db :user-id (:given-name info))))
