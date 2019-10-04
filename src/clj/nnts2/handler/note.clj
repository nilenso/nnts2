(ns nnts2.handler.note
  (:require [ring.util.response :as res]
            [nnts2.db.note :as db]
            [nnts2.model.note :as spec]
            [nnts2.spec-helpers :as spec-helper]))


(defn create [{:keys [body-params nnts-user]}]
  (prn "!!!!!!!!!!!! " body-params)
  (let [note-data (assoc body-params :created-by-id nnts-user)]
    (if (spec/valid? note-data)
      (res/response (db/create note-data))
      (spec-helper/invalid (spec/explain-str note-data)))))


(defn get-notes [{:keys [nnts-user params]}]
  (let [get-params (select-keys params [:id :title :content :created-at])
        params (assoc get-params :created-by-id nnts-user)]
    (res/response (db/get params))))
