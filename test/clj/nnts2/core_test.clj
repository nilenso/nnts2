(ns nnts2.core-test
  (:require [clojure.test :refer :all]
            [nnts2.core :refer :all]))

#_(deftest config-test
  (testing "Given a valid profile should read db-spec and server-spec from config"
    (let [config (-main "dev")]
      (is (every? #(contains? config %) [:db-spec :server-spec]))))

  (testing "Given a dev environment should read dev profile from config"
    (let [config (-main "dev")]
      (is (clojure.string/ends-with? (:db-spec config) "nnts2_dev"))))

  (testing "Given a test environment should read test profile from config"
    (let [config (-main "test")]
      (is (clojure.string/ends-with? (:db-spec config) "nnts2_test"))))

  (testing "Given an invalid profile, returned config is nil"
    (let [config (-main "fake-profile")]
      (is (nil? config)))))
