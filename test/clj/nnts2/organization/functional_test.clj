(ns nnts2.organization.functional-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.organization.handler :as handler]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(def organization {:name "Foo"
                   :slug "foo-bar"})

(deftest create-org-test

  (testing "creating organization with an invalid name should return a spec error"
    (let [body {:body (assoc organization :name 12345)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing  "creating organization with an invalid slug should return a spec error"
    (let [body {:body (assoc organization :slug 12345)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing  "creating organization with an missing name should return a spec error"
    (let [body {:body (dissoc organization :name)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing  "creating organization with a missing slug should return a spec error"
    (let [body {:body (dissoc organization :slug)}
          response (handler/create body)]
      (is (= (:status response) 400))))

  (testing  "creating organization with an valid details should return a success"
    (let [body {:body organization}
          response (handler/create body)]
      (is (= (get-in response [:body :name]) (:name organization)))
      (is (= (get-in response [:body :slug]) (:slug organization)))
      (is (= (:status response) 200)))))
