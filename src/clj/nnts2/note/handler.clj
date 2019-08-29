(ns nnts2.note.handler
  (:require [ring.util.response :as res]
            ;[clj-time.core :as time]
            [nnts2.note.db :as db]
            [nnts2.note.spec :as spec]
            [nnts2.spec-helpers :as spec-helper]))

#_(defn now [] (new java.util.Date))

(defn create [{:keys [body nnts-user]}]
  (let [note-data (assoc body :created-by-id nnts-user)]
    (if (spec/valid? note-data)
      (res/response (db/add note-data))
      (spec-helper/invalid (spec/explain-str note-data)))))
