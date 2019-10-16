(ns nnts2.note.events
  (:require [re-frame.core :as re-frame]
            [nnts2.note.api-data :as api-data]))

(enable-console-print!)

(re-frame/reg-event-db
 ::note-form-changed
 (fn [db [_ key value]]
   (assoc-in db [:note-form :data key] value)))

(re-frame/reg-event-fx
 ::note-submit
 (fn [{db :db} [_ note dir]]
   {:db         db
    :http-xhrio (api-data/create-note note dir)}))

(re-frame/reg-event-fx
 ::note-submit-success
 (fn [{db :db} [_ note]]
   {:db         (assoc db :note-form {:data {:title "" :content ""} :submit-status {}})
    :http-xhrio (api-data/get-notes (:directory-id note))}))

(re-frame/reg-event-db
 ::note-submit-failure
 (fn [db [_ note]]
   (let [err-msg (if (get-in note [:response :spec]) "invalid input"
                     (get-in note [:parse-error :original-text]))]
     (assoc-in db [:note-form :submit-status] {:error true :message err-msg}))))

(re-frame/reg-event-db
 ::note-received-list
 (fn [db [_ notes]]
   (assoc db :notes notes)))
