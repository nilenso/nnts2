(ns nnts2.member.handler-test
  (:require [nnts2.member.handler :as handler]
            [clojure.test :refer :all]
            [nnts2.user.db :as user-db]
            [nnts2.organization.db :as org-db]
            [nnts2.fixtures :refer [clear setup]]
            [clojure.string :as str]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(def user {:email       "dirk@gmail.com"
           :given-name  "Dirk"
           :family-name "Gently"
           :picture     "www.some-url.com"})

(def organization {:name "Foo"
                   :slug "foo-bar"})

(deftest add-member-to-members-table
  (testing "adding a member twice should result in a conflict and respond with status 409"
    (let [org (org-db/create {:name "test1"
                              :slug "test-1"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))
                            :role "admin"}}
          response (handler/add params)
          response-2 (handler/add params)]
      (is (= (:status response-2) 409))))

  (testing "adding a member without a role should result in an error in spec evaluation"
    (let [org (org-db/create {:name "test2"
                              :slug "test-2"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))}}
          response (handler/add params)]
      (is (= (:status response) 400)))))
