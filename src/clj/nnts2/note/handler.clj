(ns nnts2.note.handler
  (:require [ring.util.response :as res]
            ;[clj-time.core :as time]
            [nnts2.note.db :as db]
            [nnts2.note.spec :as spec]
            [nnts2.spec-helpers :as spec-helper]))

#_(defn now [] (new java.util.Date))

(defn create [request]
  (let [note-body (select-keys (:body request) [:title :content])
        user (get-in request [:session :user-info :nnts-id])
        note-data (assoc note-body :created-by-id user)]
    (if (spec/valid? note-data)
      (res/response (db/create note-data))
      (spec-helper/invalid (spec/explain-str note-data)))))



(defn getnotes [request]
  (let [user (get-in request [:session :user-info :nnts-id])
        params {:created-by-id user}]
    (res/response (db/get params))))
