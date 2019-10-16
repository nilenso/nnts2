(ns nnts2.directory.events
  (:require [re-frame.core :as re-frame]
            [nnts2.directory.api-data :as api-data]
            [nnts2.note.api-data :as note-api]))

(enable-console-print!)

(re-frame/reg-event-db
 ::received-directory-list
 (fn [db [_ directories]]
   (assoc-in db [:organization (:org-id (first directories)) :directories] directories)))

(re-frame/reg-event-fx
 ::get-directories
 (fn [cofx [_ org-id]]
   {:http-xhrio (api-data/get-directories org-id)}))

(re-frame/reg-event-fx
 ::directory-selected
 (fn [{db :db} [_ dir-id]]
   (if dir-id
     {:db         (assoc db :selected-dir dir-id)
      :http-xhrio (note-api/get-notes dir-id)}
     {:db (assoc db :selected-dir dir-id)})))

(re-frame/reg-event-db
 ::directory-add-new-subdir
 (fn [db [_ dir-id]]
   (if (= dir-id (get-in db [:add-sub-directory-form :parent-dir]))
     (assoc-in db [:add-sub-directory-form :parent-dir] nil)
     (assoc-in db [:add-sub-directory-form :parent-dir] dir-id))))

(re-frame/reg-event-fx
 ::create-directory-submit
 (fn [{db :db} [_ dir-data]]
   {:db         db
    :http-xhrio (api-data/create-directory dir-data)}))

(re-frame/reg-event-fx
 ::create-directory-success
 (fn [{db :db} [_ dir-data]]
   {:db         (assoc db :add-sub-directory-form {})
    :http-xhrio (api-data/get-directories (:org-id dir-data))}))

(re-frame/reg-event-fx
 ::create-directory-failure
 (fn [{db :db} [_ dir-data]]
   (let [err-msg (if (get-in dir-data [:response :spec]) "invalid input"
                     (get-in dir-data [:parse-error :original-text]))]
     {:db (-> db
              (assoc-in [:add-sub-directory-form :submit-status :error] true)
              (assoc-in [:add-sub-directory-form :submit-status :message] err-msg))})))
