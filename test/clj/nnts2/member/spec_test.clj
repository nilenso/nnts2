(ns nnts2.member.spec-test
  (:require [nnts2.member.spec :as member-spec]
            [clojure.test :refer :all]))

(deftest member-spec-test
  (testing "Assert org ID as a string is invalid"
    (let [member {:user-id (java.util.UUID/randomUUID) :org-id "555"}]
      (is (not (member-spec/valid? member)))))

  (testing "Assert user ID as a string is invalid"
    (let [member {:user-id "1" :org-id (java.util.UUID/randomUUID)}]
      (is (not (member-spec/valid? member)))))

  (testing "Assert org ID and user ID as strings is invalid"
    (let [member {:user-id "1" :org-id "555"}]
      (is (not (member-spec/valid? member)))))

  (testing "Assert org ID as a int is invalid"
    (let [member {:user-id (java.util.UUID/randomUUID) :org-id 555}]
      (is (not (member-spec/valid? member)))))

  (testing "Assert user ID as a int is invalid"
    (let [member {:user-id 1 :org-id (java.util.UUID/randomUUID)}]
      (is (not (member-spec/valid? member)))))

  (testing "Assert org ID and user as uuid is valid"
    (let [member {:user-id (java.util.UUID/randomUUID) :org-id (java.util.UUID/randomUUID)}]
      (is (not (member-spec/valid? member))))))
