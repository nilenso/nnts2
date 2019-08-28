(ns nnts2.organization.functional-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.organization.handler :as handler]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(def organization {:name "Foo"
                   :slug "foo-bar"})

(deftest create-org-test

  (testing "Invalid name. Organization is not created"
    (let [body {:body (assoc organization :name 12345)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Invalid slug. Organization not created"
    (let [body {:body (assoc organization :slug 12345)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Name field missing. Organization not created"
    (let [body {:body (dissoc organization :name)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Slug field missing. Organization not created"
    (let [body {:body (dissoc organization :slug)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing "Valid organization detals. Organization created"
    (let [body {:body organization}
          response (handler/create body)]
      (is (= (get-in response [:body :name]) (:name organization)))
      (is (= (get-in response [:body :slug]) (:slug organization)))
      (is (= (:status response) 200)))))
