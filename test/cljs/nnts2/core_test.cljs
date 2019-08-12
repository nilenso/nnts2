(ns nnts2.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [nnts2.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
