(ns nnts2.db.user-test
  (:require [nnts2.db.user :as db]
            [nnts2.data-factory :as factory]
            [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup)

(deftest create-user
  (let [user-data (dissoc (factory/build-user) :id)
        user      (db/create user-data)]
    (testing "Should create new user when given user data with new email"
      (is (= (set (vals (dissoc user :id))) (set (vals user-data)))))

    (testing "Should update name when given data with same email id but different name"
      (let [new-given-name "Ford"
            new-data       (assoc user-data :given-name new-given-name)
            edited-user    (db/create new-data)]
        (is (= (dissoc edited-user :first-name) (dissoc user :first-name)))
        (is (= (:first-name edited-user) (:given-name new-data)))))

    (testing "Add a new user when given same user data but with different email"
      (let [new-email "random@gmail.com"
            new-data  (assoc user-data :email new-email)
            new-user  (db/create new-data)]
        (is (not (= (:id user) (:id new-user))))))))

(deftest get-by-email-test
  (let [user-data (dissoc (factory/build-user) :id)
        user      (db/create user-data)]
    (testing "Should give correct user when given user's email"
      (let [user-row (dissoc (db/get-by-email (:email user-data)) :id)]
        (is (= (set (vals user-row)) (set (vals user-data))))))))

(deftest get-by-id-test
  (let [user-data (dissoc (factory/build-user) :id)
        user      (db/create user-data)]
    (testing "Should give correct user when given user's id"
      (let [user-row (db/get-by-id (:id user))]
        (is (= user user-row))))))
