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

(deftest add-member-handler-test
  (testing "adding a member twice should result in a conflict and return with status 409"
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
      (is (= (:status response) 400))))

  (testing "adding a member with the correct information should return the record with a 200"
    (let [org (org-db/create {:name "test3"
                              :slug "test-3"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))
                            :role "admin"}}
          response (handler/add params)]
      (is (= (:status response) 200))))

  (testing "adding a member with an invalid role should return in spec error with 400"
    (let [org (org-db/create {:name "test4"
                              :slug "test-4"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))
                            :role "owner"}}
          response (handler/add params)]
      (is (= (:status response) 400)))))
