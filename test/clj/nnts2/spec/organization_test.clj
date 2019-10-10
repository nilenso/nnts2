(ns nnts2.handler.organization-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.data-factory :as factory]
            [nnts2.spec.organization :as spec]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(deftest org-spec-test
  (testing "should succeed if name and slug are strings"
    (let [data  {:name "name" :slug "slug"}
          valid (spec/valid? data)]
      (is valid)))
  (testing "should fail if name is not string"
    (let [data  {:name 1234 :slug "slug"}
          valid (spec/valid? data)]
      (is (false? valid))))
  (testing "should fail if slug is not string"
    (let [data  {:name "name" :slug 65363}
          valid (spec/valid? data)]
      (is (false? valid))))
  (testing "should fail if name is not present"
    (let [data  {:slug "slug"}
          valid (spec/valid? data)]
      (is (false? valid))))
  (testing "should fail if slug is not present"
    (let [data  {:name "name"}
          valid (spec/valid? data)]
      (is (false? valid)))))


#_(deftest create-org-test
    (testing "creating organization with an invalid name should return a spec error"
      (let [body     (assoc (factory/build-organization) :name 12345)
            response (handler/create {} body)]
        (is (= (:status response) 400))))

    (testing "creating organization with an invalid slug should return a spec error"
      (let [body     (assoc (factory/build-organization) :slug 12345)
            response (handler/create {} body)]
        (is (= (:status response) 400))))

    (testing "creating organization with an missing name should return a spec error"
      (let [body     (dissoc (factory/build-organization) :name)
            response (handler/create {} body)]
        (is (= (:status response) 400))))

    (testing "creating organization with a missing slug should return a spec error"
      (let [body     (dissoc (factory/build-organization) :slug)
            response (handler/create {} body)]
        (is (= (:status response) 400))))

    (testing "creating organization with valid details should return a success"
      (let [user     (user-db/create (user-factory))
            body     (factory/build-organization)
            response (handler/create {:nnts-user (:id user)} body)]
        (is (= (get-in response [:body :name]) (:name body)))
        (is (= (get-in response [:body :slug]) (:slug body)))
        (is (= (:status response) 200))))

    (testing "creating organization with a slug that already exists should return a 409"
      (let [user       (user-db/create (user-factory))
            req        {:nnts-user (:id user)}
            body       (factory/build-organization)
            response   (handler/create req body)
            response-2 (handler/create req body)]
        (is (= (:status response-2) 409)))))

(deftest add-member-handler-test
  (testing "adding a member twice should result in a conflict and return with status 409"
    (let [org        (db/create {:name "test1"
                                 :slug "test-1"})
          user       (user-db/create (user-factory))
          params     {:params {:user-id (str (:id user))
                               :org-id  (str (:id org))
                               :role    "admin"}}
          response   (handler/add-user params)
          response-2 (handler/add-user params)]
      (is (= (:status response-2) 409))))

  (testing "adding a member without a role should result in an error in spec evaluation"
    (let [org      (db/create {:name "test2"
                               :slug "test-2"})
          user     (user-db/create (user-factory))
          params   {:params {:user-id (str (:id user))
                             :org-id  (str (:id org))}}
          response (handler/add-user params)]
      (is (= (:status response) 400))))

  (testing "adding a member with the correct information should return the record with a 200"
    (let [org      (db/create {:name "test3"
                               :slug "test-3"})
          user     (user-db/create (user-factory))
          params   {:params {:user-id (str (:id user))
                             :org-id  (str (:id org))
                             :role    "admin"}}
          response (handler/add-user params)]
      (is (= (:status response) 200))))

  (testing "adding a member with an invalid role should return in spec error with 400"
    (let [org      (db/create {:name "test4"
                               :slug "test-4"})
          user     (user-db/create (user-factory))
          params   {:params {:user-id (str (:id user))
                             :org-id  (str (:id org))
                             :role    "owner"}}
          response (handler/add-user params)]
      (is (= (:status response) 400)))))
