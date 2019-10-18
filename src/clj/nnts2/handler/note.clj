(ns nnts2.handler.note
  (:require [ring.util.response :as res]
            [nnts2.db.note :as db]))

(defn create [{:keys [body-params nnts-user]} dir-id]
  (let [note-data (assoc body-params
                         :created-by-id nnts-user
                         :directory-id dir-id)]
    (res/response (db/create note-data))))

(defn get-notes [{:keys [nnts-user]} dir-id]
  (let [filter-params {:directory-id dir-id}]
    (res/response (db/get filter-params))))
