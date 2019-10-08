(ns nnts2.organization.functional-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.db.user :as user-db]
            [nnts2.handler.organization :as handler]
            [nnts2.db.organization :as db])
  (:import (java.util UUID)))

(use-fixtures :each clear)
(use-fixtures :once setup)

(defn organization-factory []  {:name "Foo"
                                :slug "foo-bar"})

(defn user-factory []  {:email       "dirk@gmail.com"
                        :given-name  "Dirk"
                        :family-name "Gently"
                        :picture     "www.some-url.com"})

(deftest create-org-test
  (testing "creating organization with an invalid name should return a spec error"
    (let [body (assoc (organization-factory) :name 12345)
          response (handler/create {} body)]
      (is (= (:status response) 400))))

  (testing "creating organization with an invalid slug should return a spec error"
    (let [body (assoc (organization-factory) :slug 12345)
          response (handler/create {} body)]
      (is (= (:status response) 400))))

  (testing "creating organization with an missing name should return a spec error"
    (let [body (dissoc (organization-factory) :name)
          response (handler/create {} body)]
      (is (= (:status response) 400))))

  (testing "creating organization with a missing slug should return a spec error"
    (let [body (dissoc (organization-factory) :slug)
          response (handler/create {} body)]
      (is (= (:status response) 400))))

  (testing "creating organization with valid details should return a success"
    (let [user (user-db/create (user-factory))
          body (organization-factory)
          response (handler/create {:nnts-user (:id user)} body)]
      (is (= (get-in response [:body :name]) (:name body)))
      (is (= (get-in response [:body :slug]) (:slug body)))
      (is (= (:status response) 200))))

  (testing "creating organization with a slug that already exists should return a 409"
    (let [user (user-db/create (user-factory))
          req {:nnts-user (:id user)}
          body (organization-factory)
          response (handler/create req body)
          response-2 (handler/create req body)]
      (is (= (:status response-2) 409)))))

(deftest add-member-handler-test
  (testing "adding a member twice should result in a conflict and return with status 409"
    (let [org (db/create {:name "test1"
                          :slug "test-1"})
          user (user-db/create (user-factory))
          params {:params {:user-id (str (:id user))
                           :org-id  (str (:id org))
                           :role    "admin"}}
          response (handler/add-user params)
          response-2 (handler/add-user params)]
      (is (= (:status response-2) 409))))

  (testing "adding a member without a role should result in an error in spec evaluation"
    (let [org (db/create {:name "test2"
                          :slug "test-2"})
          user (user-db/create (user-factory))
          params {:params {:user-id (str (:id user))
                           :org-id  (str (:id org))}}
          response (handler/add-user params)]
      (is (= (:status response) 400))))

  (testing "adding a member with the correct information should return the record with a 200"
    (let [org (db/create {:name "test3"
                          :slug "test-3"})
          user (user-db/create (user-factory))
          params {:params {:user-id (str (:id user))
                           :org-id  (str (:id org))
                           :role    "admin"}}
          response (handler/add-user params)]
      (is (= (:status response) 200))))

  (testing "adding a member with an invalid role should return in spec error with 400"
    (let [org (db/create {:name "test4"
                          :slug "test-4"})
          user (user-db/create (user-factory))
          params {:params {:user-id (str (:id user))
                           :org-id  (str (:id org))
                           :role    "owner"}}
          response (handler/add-user params)]
      (is (= (:status response) 400)))))
