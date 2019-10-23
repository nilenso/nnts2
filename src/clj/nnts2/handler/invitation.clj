(ns nnts2.handler.invitation
  (:require [ring.util.response :as res]
            [nnts2.model.invitation :as model]
            [nnts2.utils.http-utils :as utils]))

(defn create [{:keys [nnts-user]} body org-id]
  (let [invite-data (conj body {:created-by-id nnts-user
                                :org-id        org-id})]
    (utils/create-response
     (model/create invite-data))))

(defn get-invites-for-org [{:keys [nnts-user]} org-id]
  (utils/create-response
   (model/list org-id nnts-user)))

(defn update
  "not implemented right now"
  [{:keys [nnts-user]} body org-id invite-id]
  (let [where-params {:id invite-id}]
    (utils/create-response
     (model/update body where-params nnts-user org-id))))

(defn respond-to-invite [{:keys [nnts-user]} invite-id status]
  (utils/create-response
   (model/respond-to-invite invite-id nnts-user status)))
