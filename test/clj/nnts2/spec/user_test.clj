(ns nnts2.spec.user-test
  (:require [nnts2.spec.user :as user-spec]
            [clojure.test :refer :all]
            [nnts2.data-factory :as factory]))


(def user (factory/build-user))

(deftest spec-requirements
  (testing "Missing email"
    (let [user        (dissoc user :email)
          is-valid    (user-spec/valid? user)
          explain-str (user-spec/explain-str user)]
      (is (not is-valid))
      (is (contains? explain-str :clojure.spec.alpha/problems)))))

(deftest user-info-validity
  (testing "Valid user info"
    (is (user-spec/valid? user)))

  (testing "Invalid email"
    (let [user (assoc user :email "2.com")]
      (is (not (user-spec/valid? user)))))

  (testing "Empty given name"
    (let [user (assoc user :given-name "")]
      (is (not (user-spec/valid? user)))))

  (testing "Invalid family name"
    (let [user (assoc user :family-name "1245")]
      (is (not (user-spec/valid? user)))))

  (testing "Invalid picture URL"
    (let [user (assoc user :picture "1245")]
      (is (not (user-spec/valid? user))))))
