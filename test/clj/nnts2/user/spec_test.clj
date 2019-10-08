(ns nnts2.user.spec-test
  (:require [clojure.test :refer :all]
            [nnts2.model.user :as spec]))

(def user {:given-name  "Dirk"
           :family-name "Gently"
           :email       "dirkgently@agency.com"
           :picture     "https://google.com"})

(deftest spec-requirements
  (testing "Missing email"
    (let [user        (dissoc user :email)
          is-valid    (spec/valid? user)
          explain-str (spec/explain-str user)]
      (is (not is-valid))
      (is (contains? explain-str :clojure.spec.alpha/problems)))))

(deftest user-info-validity

  (testing "Valid user info"
    (is (spec/valid? user)))

  (testing "Invalid email"
    (let [user (assoc user :email "2.com")]
      (is (not (spec/valid? user)))))

  (testing "Empty given name"
    (let [user (assoc user :given-name "")]
      (is (not (spec/valid? user)))))

  (testing "Invalid family name"
    (let [user (assoc user :family-name "1245")]
      (is (not (spec/valid? user)))))

  (testing "Invalid picture URL"
    (let [user (assoc user :picture "1245")]
      (is (not (spec/valid? user))))))
