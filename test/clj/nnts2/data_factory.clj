(ns nnts2.data-factory
  (:require  [clojure.test :as t]
             [nnts2.db.user :as user-db]
             [nnts2.db.organization :as org-db]
             [nnts2.model.organization :as org])
  (:import (java.util UUID)))

(def user-id (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff"))

(defn get-uuid [] (UUID/randomUUID))

(defn user
  ([] (user "Dirk" "Gently" "dirk@gmail.com" "www.url.com"))
  ([given-name family-name email picture-url]
   {:id user-id
    :given-name given-name
    :family-name family-name
    :email email
    :picture-url picture-url}))


(defn organization
  ([] (organization "org" "slug"))
  ([name] (organization name  name))
  ([name slug] {:name name :slug slug}))

(defn membership
  ([org-id] (membership org-id "member" user-id))
  ([org-id role] (membership org-id role user-id))
  ([org-id role new-user-id]
   {:org-id org-id
    :role role
    :user-id new-user-id}))

(defn create-org
  "creates org and a membership entry also"
  ([] (create-org "default-org" "default-slug"))
  ([name slug](let [org-id (:id (org-db/create (organization name slug)))
                    member-data (membership org-id)
                    member (org-db/add-user member-data)]
                org-id)))

(defn directory
  ([name]
   (directory name (create-org)))
  ([name org-id]
   (directory name org-id nil))
  ([name org-id parent-id]
   {:name name
    :parent-id parent-id
    :org-id org-id
    :created-by-id user-id}))


(defn nested-directory-rows [nest-level]
  (let [org-id "fake-org"]
    (loop [level 1
           parent-id nil
           rows []]
      (if (> level nest-level)
        rows
        (recur (+ level 1) level (conj rows (assoc (directory "dir" org-id parent-id) :id level)))))))
