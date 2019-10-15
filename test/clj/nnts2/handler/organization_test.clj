(ns nnts2.handler.organization-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.data-factory :as factory]
            [nnts2.handler.organization :as handler])
  (:import (java.util UUID)))

(use-fixtures :each clear)
(use-fixtures :once setup)

(deftest create-org-test
  (testing "should give 409 response if model/create org returns error keyword in map"
    (with-redefs [nnts2.model.organization/create-org-with-member (fn  [a b] {:error :some-random-error})]
      (let [request          {:nnts-user (factory/get-uuid)}
            body             (factory/build-organization)
            {:keys [status]} (handler/create request body)]
        (is (= status 409)))))

  (testing "should give 200 response from model/create org if no error but id found in map"
    (with-redefs [nnts2.model.organization/create-org-with-member (fn  [a b] {:id "abc"})]
      (let [request          {:nnts-user (factory/get-uuid)}
            body             (factory/build-organization)
            {:keys [status]} (handler/create request body)]
        (is (= status 200))))))
