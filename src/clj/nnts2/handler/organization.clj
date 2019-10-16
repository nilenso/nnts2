(ns nnts2.handler.organization
  (:require [nnts2.db.organization :as db]
            [nnts2.model.organization :as model]
            [ring.util.response :as res]))

(defn create
  "Create an organization and make the creator an admin"
  [{:keys [nnts-user]} body]
  (let [org (model/create-org-with-member body nnts-user)]
    (condp #(contains? %2 %1) org
      :error (-> (res/response (str (:error org)))
                 (res/status 409))
      :id    (-> (res/response org)
                 (res/status 200)))))

(defn get-orgs-for-member [{:keys [nnts-user]}]
  (res/response (model/get-membership-orgs {:user-id nnts-user})))
