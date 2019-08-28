(ns nnts2.note.handler
  (:require [ring.util.response :as res]
            ;[clj-time.core :as time]
            [nnts2.note.db :as db]
            [nnts2.note.spec :as spec]))

#_(defn now [] (new java.util.Date))

(defn create [request]
  (let [note-body (select-keys (:body request) [:title :content])
        user (get-in request [:session :user-info :nnts-id])
        note-data (assoc note-body :created-by-id user)]
    (if (spec/valid? note-data)
      (res/response (db/add note-data))
       "invalid")))
