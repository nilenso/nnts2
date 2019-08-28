(ns nnts2.note.handler
  (:require [ring.util.response :as res]
            ;[clj-time.core :as time]
            [nnts2.note.db :as db]
            [nnts2.note.spec :as spec_]))

#_(defn now [] (new java.util.Date))

(defn create [request]
  (let [note-body (select-keys (:body request) [:title :content])
        note-data (assoc note-body :created-by-id 1)]
    (if (spec_/valid? note-data)
      (res/response (db/add note-data))
      "inv")))
