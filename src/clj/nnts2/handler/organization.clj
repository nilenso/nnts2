(ns nnts2.handler.organization
  (:require [nnts2.db.organization :as db]
            [nnts2.spec-helpers :as spec-helper]
            [nnts2.model.organization-spec :as spec]
            [nnts2.model.member-spec :as member-spec]
            [ring.util.response :as res])
  (:import (java.util UUID)))

(defn create
  "Create an organization and make the creator an admin"
  [{:keys [nnts-user] :as request} body]
  (if (spec/valid? body)
    (let [org (db/create body)
          member {:user-id nnts-user
                  :org-id  (:id org)
                  :role    "admin"}]
      (if (nil? org)
        (-> (res/response "Slug exists")
            (res/status 409))
        (do (db/add-user member)
            (res/response org))))
    (spec-helper/invalid (spec/explain-str body))))

(defn add-user [{:keys [params]}]
  (let [params {:user-id (UUID/fromString (:user-id params))
                :org-id  (UUID/fromString (:org-id params))
                :role    (:role params)}]
    (if (member-spec/valid? params)
      (let [member (db/add-user params)]
        (if (nil? member)
          (-> (res/response "User is already a member of this organization")
              (res/status 409))
          (res/response member)))
      (spec-helper/invalid (spec/explain-str params)))))

(defn get-orgs [{:keys [nnts-user]}]
  (let [orgs (db/get-by-user-id nnts-user)]
    (res/response orgs)))
