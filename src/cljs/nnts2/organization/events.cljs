(ns nnts2.organization.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [nnts2.user.events]
   [nnts2.directory.api-data :as dir-api]
   [nnts2.organization.api-data :as api-data]))

(re-frame/reg-event-fx
 ::create-organization
 (fn [{db :db}  [_ org-details]]
   {:http-xhrio (api-data/create-org org-details)}))

(re-frame/reg-event-db
 ::organization-created
 (fn [db [_ org-details]]
   (-> db
       (assoc-in [:organization (:id org-details)] (dissoc org-details :id))
       (assoc-in [:organization :show-create-org-form] false))))

(re-frame/reg-event-fx
 ::organizations-retrieved
 (fn [{db :db} [_ org-details]]
   {:http-xhrio (map #(dir-api/get-directories (:id %)) org-details)
    :db         (assoc db :organization
                       (reduce
                        (fn [acc {:keys [id] :as org}] (assoc acc id org))
                        (:organization db)
                        org-details))}))

(re-frame/reg-event-db
 ::show-create-org-form
 (fn [db [_ show-form]]
   (assoc-in db [:organization :show-create-org-form] (not (get-in db [:organization :show-create-org-form])))))
