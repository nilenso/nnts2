(ns nnts2.organization.functional-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.organization.handler :as handler]
            [nnts2.user.db :as user-db]
            [nnts2.organization.db :as db]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(def organization {:name "Foo"
                   :slug "foo-bar"})

(def user {:email       "dirk@gmail.com"
           :given-name  "Dirk"
           :family-name "Gently"
           :picture     "www.some-url.com"})

(deftest create-org-test

  (testing "Invalid name. Organization is not created"
    (let [body {:body (assoc organization :name 12345)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Invalid slug. Organization not created"
    (let [body {:body (assoc organization :slug 12345)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Name field missing. Organization not created"
    (let [body {:body (dissoc organization :name)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Slug field missing. Organization not created"
    (let [body {:body (dissoc organization :slug)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Valid organization detals. Organization created"
    (let [body {:body organization}
          response (handler/create body)]
      (is (= (get-in response [:body :name]) (:name organization)))
      (is (= (get-in response [:body :slug]) (:slug organization)))
      (is (= (:status response) 200)))))

(deftest add-member-handler-test
  (testing "adding a member twice should result in a conflict and return with status 409"
    (let [org (db/create {:name     "test1"
                              :slug "test-1"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))
                            :role "admin"}}
          response (handler/add params)
          response-2 (handler/add params)]
      (is (= (:status response-2) 409))))

  (testing "adding a member without a role should result in an error in spec evaluation"
    (let [org (db/create {:name "test2"
                              :slug "test-2"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))}}
          response (handler/add params)]
      (is (= (:status response) 400))))

  (testing "adding a member with the correct information should return the record with a 200"
    (let [org (db/create {:name "test3"
                              :slug "test-3"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))
                            :role "admin"}}
          response (handler/add params)]
      (is (= (:status response) 200))))

  (testing "adding a member with an invalid role should return in spec error with 400"
    (let [org (db/create {:name "test4"
                              :slug "test-4"})
          user (user-db/create user)
          params {:params  {:user-id (str (:id user))
                            :org-id (str (:id org))
                            :role "owner"}}
          response (handler/add params)]
      (is (= (:status response) 400)))))

