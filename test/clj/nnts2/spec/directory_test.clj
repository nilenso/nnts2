(ns nnts2.spec.directory-test
  (:require [clojure.test :refer :all]
            [nnts2.spec.directory :as spec]
            [nnts2.data-factory :as factory]
            [nnts2.fixtures :as fixtures]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup)

(deftest directory-spec-test
  (testing "should succeed if name is string (only alphanumeric and -), parent id is uuid"
    (let [data  {:name "name" :parent-id (factory/get-uuid)}
          valid (spec/valid? data)]
      (is valid)))

  (testing "should succesed if org id is a valid uuid"
    (let [data  (factory/get-uuid)
          valid (spec/org-valid? data)]
      (is valid)))

  (testing "should return false if org id is nil or not uuid"
    (let [data   "random string"
          valid  (spec/valid? data)
          data   nil
          valid2 (spec/org-valid? data)]
      (is (false? valid))
      (is (false? valid2))))

  (testing "should return false if dir name is not a string"
    (let [data  {:name 1234 :parent-id (factory/get-uuid)}
          valid (spec/valid? data)]
      (is (false? valid))))

  (testing "should fail if dir name has more than alphabets, numerals and -"
    (let [data  {:name "name 123" :parent-id (factory/get-uuid)}
          valid (spec/valid? data)]
      (is (false? valid))))

  (testing "should succeed if parent id is either uuid or nil in case of root directory"
    (let [data1  {:name "dir-name" :parent-id nil}
          valid1 (spec/valid? data1)
          data2  {:name "dir-name" :parent-id (factory/get-uuid)}
          valid2 (spec/valid? data1)]
      (is valid1)
      (is valid2)))

  (testing "should suceed even if parent id is not in dir data"
    (let [data  {:name "dir-name"}
          valid (spec/valid? data)]
      (is valid))))
