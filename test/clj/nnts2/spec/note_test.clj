(ns nnts2.spec.note-test
  (:require [nnts2.spec.note :as spec]
            [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]
            [nnts2.data-factory :as factory]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup)

(deftest note-spec-test
  (testing "should succeed if given non empty strings for both title and content"
    (let [data  {:title "title" :content "content"}
          valid (spec/valid? data)]
      (is valid)))

  (testing "should fail if given non string title"
    (let [data  {:title 1234 :content "content"}
          valid (spec/valid? data)]
      (is (false? valid))))

  (testing "should fail if given empty title"
    (let [data   {:title "" :content "content"}
          valid  (spec/valid? data)
          data   {:title nil :content "content"}
          valid2 (spec/valid? data)]
      (is (false? valid))
      (is (false? valid2))))

  (testing "should fail if given empty content"
    (let [data   {:title "title" :content ""}
          valid  (spec/valid? data)
          data   {:title "title" :content nil}
          valid2 (spec/valid? data)]
      (is (false? valid))
      (is (false? valid2)))))
