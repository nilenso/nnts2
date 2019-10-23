(ns nnts2.model.invitation
  (:require [nnts2.db.invitation :as db]
            [nnts2.model.organization :as org-model]
            [nnts2.model.user :as user-model]))

(defn create [{:keys [org-id created-by-id] :as params}]
  "invite a new user via email id to join an organization in the given role"
  (if (org-model/is-admin-of-org created-by-id org-id)
    (db/create params)
    {:error :need-to-be-admin}))

(defn update [update-data where-params nnts-user org-id]
  (if (org-model/is-admin-of-org nnts-user org-id)
    (db/update update-data where-params)
    {:error :need-to-be-admin}))

(defn respond-to-invite
  "accept or decline an invite"
  [invite-id nnts-user status]
  (let [email (-> nnts-user
                  (user-model/get-by-id)
                  :email)]
    (if (empty? (db/get {:id     invite-id
                         :email  email
                         :status "invited"}))
      {:error :invite-doesnt-exist}
      (let [updated-invite (db/update
                            {:status status}
                            {:id invite-id})]
        (if (= status "accepted")
          (do (org-model/add-member {:org-id  (:org-id updated-invite)
                                     :role    (:invite-for-role updated-invite)
                                     :user-id nnts-user})
              {:message "Joined organization successfully"})
          {:message "Declined invitation"})))))

(defn list [org-id nnts-user]
  (if (org-model/is-admin-of-org nnts-user org-id)
    (db/get {:org-id org-id})
    {:error :need-to-be-admin}))
