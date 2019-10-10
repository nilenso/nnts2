(ns nnts2.handler.organization-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.data-factory :as factory]
            [nnts2.handler.organization :as handler])
  (:import (java.util UUID)))

(use-fixtures :each clear)
(use-fixtures :once setup)

#_(deftest create-org-test
    (testing "creating organization with valid details should return a success"
      (let [user     (factory/create-user)
            body     (factory/build-organization)
            response (handler/create {:nnts-user (:id user)} body)]
        (is (= (get-in response [:body :name]) (:name body)))
        (is (= (get-in response [:body :slug]) (:slug body)))
        (is (= (:status response) 200))))

    (testing "creating organization with a slug that already exists should return a 409"
      (let [user       (factory/create-user)
            req        {:nnts-user (:id user)}
            body       (factory/build-organization)
            response   (handler/create req body)
            response-2 (handler/create req body)]
        (is (= (:status response-2) 409)))))

#_(deftest add-member-handler-test
    (testing "adding a member twice should result in a conflict and return with status 409"
      (let [user       (factory/create-user)
            org        (handler/create
                        {:nnts-user (:id user)}
                        (factory/build-organization))
            params     {:params {:user-id (str (:id user))
                                 :org-id  (str (:id (:body org)))
                                 :role    "admin"}}
            response   (handler/add-user params)
            response-2 (handler/add-user params)]
        (is (= (:status response-2) 409))))

    (testing "adding a member without a role should result in an error in spec evaluation"
      (let [org      (db/create {:name "test2"
                                 :slug "test-2"})
            user     (user-db/create (factory/build-user))
            params   {:params {:user-id (str (:id user))
                               :org-id  (str (:id org))}}
            response (handler/add-user params)]
        (is (= (:status response) 400))))

    (testing "adding a member with the correct information should return the record with a 200"
      (let [org      (db/create {:name "test3"
                                 :slug "test-3"})
            user     (user-db/create (factory/build-user))
            params   {:params {:user-id (str (:id user))
                               :org-id  (str (:id org))
                               :role    "admin"}}
            response (handler/add-user params)]
        (is (= (:status response) 200))))

    (testing "adding a member with an invalid role should return in spec error with 400"
      (let [org      (db/create {:name "test4"
                                 :slug "test-4"})
            user     (user-db/create (factory/build-user))
            params   {:params {:user-id (str (:id user))
                               :org-id  (str (:id org))
                               :role    "owner"}}
            response (handler/add-user params)]
        (is (= (:status response) 400)))))

(deftest create-org-test)
(deftest get-orgs-test
  (testing "Should restructure request correctly to pass on authenticated user id"))
