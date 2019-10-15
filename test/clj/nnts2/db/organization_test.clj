(ns nnts2.db.organization-test
  (:require [nnts2.db.organization :as db]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.data-factory :as factory]
            [clojure.test :refer :all]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(deftest create-org-test
  (testing "should correctly create new org when a non-conflicting slug is given"
    (let [org-data (factory/build-organization)
          org      (db/create org-data)]
      (is (not (nil? org)))))

  (testing "should raise exception if a conflicting slug is given"
    (let [org-data (factory/build-organization)]
      (is (thrown? Exception (db/create org-data))))))

(deftest add-user-test
  (let [org-data (factory/build-organization)
        org      (db/create org-data)]
    (testing "should add to membership row when given valid user, org and role"
      (let [user        (factory/create-user)
            member-data {:user-id (:id user)
                         :org-id  (:id org)
                         :role    "admin"}
            response    (db/add-user member-data)]
        (is (map? response))))

    (testing "should return nil due to conflict when adding a user-org to membership table twice"
      (let [user        (factory/create-user "new1@gmail.com")
            member-data {:user-id (:id user)
                         :org-id  (:id org)
                         :role    "member"}
            response    (db/add-user member-data)
            response2   (db/add-user member-data)]
        (is (nil? response2))))

    (testing "should raise exception when given invalid role"
      (let [user        (factory/create-user "new2@gmail.com")
            member-data {:user-id (:id user)
                         :org-id  (:id org)
                         :role    "newmember"}]
        (is (thrown? Exception (db/add-user member-data)))))))

(deftest get-by-userid-test
  (let [user (factory/create-user)]
    (testing "should give one organization when a user is member of single org"
      (let [org1-data    (factory/build-organization "org1" "slug1")
            org1         (db/create org1-data)
            mem1         (db/add-user (factory/build-membership (:id org1) "admin" (:id user)))
            get-response (db/get-by-user-id (:id user))]
        (is (= 1 (count get-response)))))

    (testing "should give two organization when a user is member of two org"
      (let [org2-data    (factory/build-organization "org2" "slug2")
            org3-data    (factory/build-organization "org3" "slug3")
            org2         (db/create org2-data)
            org3         (db/create org3-data)
            mem2         (db/add-user (factory/build-membership (:id org2) "admin" (:id user)))
            mem3         (db/add-user (factory/build-membership (:id org3) "admin" (:id user)))
            get-response (db/get-by-user-id (:id user))]
        (is (= 3 (count get-response)))))))
