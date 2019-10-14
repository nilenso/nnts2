(ns nnts2.handler.note
  (:require [ring.util.response :as res]
            [nnts2.db.note :as db]
            [nnts2.model.note :as spec]
            [nnts2.spec-helpers :as spec-helper]))

(defn create [{:keys [body-params nnts-user]} dir-id]
  (let [note-data (assoc body-params
                         :created-by-id nnts-user
                         :directory-id dir-id)]
    (if (spec/valid? note-data)
      (res/response (db/create note-data))
      (spec-helper/invalid (spec/explain-str note-data)))))

(defn get-notes [{:keys [nnts-user]} dir-id]
  (let [filter-params {:created-by-id nnts-user
                       :directory-id  dir-id}]
    (res/response (db/get filter-params))))
