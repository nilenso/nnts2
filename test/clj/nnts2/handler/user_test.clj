(ns nnts2.handler.user-test
  (:require [nnts2.handler.user :as handler]
            [nnts2.data-factory :as factory]
            [nnts2.fixtures :as fixtures]
            [clojure.test :refer :all]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup)

(deftest create-user
  (with-redefs []))

(deftest user-info-test
  (testing "get correct user given id"))


                                        ;TODO
