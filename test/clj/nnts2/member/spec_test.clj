(ns nnts2.member.spec-test
  (:require [nnts2.member.spec :as member-spec]
            [clojure.test :refer :all]))

(deftest member-spec-test
  (testing "org ID as a string is invalid"
    (let [member {:user-id (java.util.UUID/randomUUID)
                  :org-id "555"
                  :role "admin"}]
      (is (not (member-spec/valid? member)))))

  (testing "user ID as a string is invalid"
    (let [member {:user-id "1"
                  :org-id (java.util.UUID/randomUUID)
                  :role "guest"}]
      (is (not (member-spec/valid? member)))))

  (testing "org ID and user ID as strings is invalid"
    (let [member {:user-id "1"
                  :org-id "555"
                  :role "guest"}]
      (is (not (member-spec/valid? member)))))

  (testing "org ID as a int is invalid"
    (let [member {:user-id (java.util.UUID/randomUUID)
                  :org-id 555
                  :role "admin"}]
      (is (not (member-spec/valid? member)))))

  (testing "user ID as a int is invalid"
    (let [member {:user-id 1
                  :org-id (java.util.UUID/randomUUID)
                  :role "admin"}]
      (is (not (member-spec/valid? member)))))

  (testing "org ID and user as uuid is valid"
    (let [member {:user-id (java.util.UUID/randomUUID)
                  :org-id (java.util.UUID/randomUUID)
                  :role "member"}]
      (is (member-spec/valid? member))))

  (testing "role is invalid"
    (let [member {:user-id (java.util.UUID/randomUUID)
                  :org-id (java.util.UUID/randomUUID)
                  :role "oowner"}]
      (is (not (member-spec/valid? member))))))
