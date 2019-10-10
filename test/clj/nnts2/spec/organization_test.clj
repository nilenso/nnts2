(ns nnts2.spec.organization-test
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
