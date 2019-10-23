(ns nnts2.db.invitation-test
  (:require [clojure.test :refer :all]
            [nnts2.db.invitation :as db]
            [nnts2.fixtures :as fixtures]
            [nnts2.data-factory :as factory]
            [honeysql.core :as sql]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(deftest create-invite-test
  (let [userid factory/user-id
        org    (:id (factory/create-org))]
    (testing "should be able to invite an email given existing org and user"
      (let [invite-data (factory/build-invitation
                         "invitee@gmail.com"
                         "member"
                         org
                         userid)
            invite      (db/create invite-data)]
        (is (=
             (assoc invite-data :status "invited")
             (select-keys invite [:email :created-by-id :org-id :invite-for-role :status])))))

    (testing "should give error if given invalid role"
      (let [invite-data (factory/build-invitation
                         "invitee@gmail.com"
                         "random-role"
                         org
                         userid)]
        (is (thrown? Exception (db/create invite-data)))))))

(deftest create-invite-failure-test
  (let [userid factory/user-id
        org    (:id (factory/create-org))]
    (testing "should give error if given invalid status"
      (let [invite-data (assoc (factory/build-invitation
                                "invitee2@gmail.com"
                                "member"
                                org
                                userid)
                               :status
                               "pending")]
        (is (thrown? Exception (db/create invite-data)))))))

(deftest get-invite-test
  (let [userid factory/user-id
        org    (:id (factory/create-org))]
    (testing "should give zero invite when zero invites have been created"
      (let [invites (db/get {:org-id org})]
        (is (empty? invites))))

    (testing "should give one invite when one invite has been created"
      (let [invite-data     (factory/build-invitation
                             "invitee1@gmail.com"
                             "member"
                             org
                             userid)
            invite          (db/create invite-data)
            invite-received (db/get {:org-id org})]
        (is (= 1 (count invite-received)))))

    (testing "should give one invite for org if second invite is created for different org"
      (let [org2                    (:id (factory/create-org "second" "second"))
            invite-data             (factory/build-invitation
                                     "invitee1@gmail.com"
                                     "member"
                                     org2
                                     userid)
            invite                  (db/create invite-data)
            invite-received-for-org (db/get {:org-id org})
            total-invites           (db/get {})
            member-invites          (db/get {:invite-for-role "member"})]
        (is (= 1 (count invite-received-for-org)))
        (is (= 2 (count total-invites)))
        (is (= 2 (count member-invites)))))))
