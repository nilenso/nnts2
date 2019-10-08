(ns nnts2.data-factory
  (:require  [clojure.test :as t]
             [nnts2.db.user :as user-db]
             [nnts2.db.organization :as org-db]
             [nnts2.model.organization :as org])
  (:import (java.util UUID)))

(def user-id (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff"))

(defn get-uuid [] (UUID/randomUUID))

(defn build-user
  ([] (build-user "Dirk" "Gently" "dirk@gmail.com" "www.url.com"))
  ([given-name family-name email picture-url]
   {:id user-id
    :given-name given-name
    :family-name family-name
    :email email
    :picture-url picture-url}))

(defn build-organization
  ([] (build-organization "org" "slug"))
  ([name] (build-organization name  name))
  ([name slug] {:name name :slug slug}))

(defn build-membership
  ([org-id] (build-membership org-id "member" user-id))
  ([org-id role] (build-membership org-id role user-id))
  ([org-id role new-user-id]
   {:org-id org-id
    :role role
    :user-id new-user-id}))

(defn create-org
  "creates org and a membership entry also"
  ([] (create-org "default-org" "default-slug"))
  ([name slug] (let [org-id (:id (org-db/create (build-organization name slug)))
                     member-data (build-membership org-id)
                     member (org-db/add-user member-data)]
                 org-id)))

(defn build-directory
  ([name]
   (build-directory name (create-org)))
  ([name org-id]
   (build-directory name org-id nil))
  ([name org-id parent-id]
   {:name name
    :parent-id parent-id
    :org-id org-id
    :created-by-id user-id}))

(defn build-nested-directory-rows [nest-level]
  (let [org-id "orgid"]
    (map
     (fn [level] (assoc
                  (build-directory "dir" org-id (when (> level 0) level))
                  :id (inc level)))
     (range 0 nest-level))))
