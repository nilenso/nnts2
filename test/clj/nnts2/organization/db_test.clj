(ns nnts2.organization.db-test
  (:require [nnts2.db.organization :as db]
            [nnts2.db.user :as user-db]
            [nnts2.fixtures :refer [clear setup]]
            [clojure.test :refer :all]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(deftest add-member-to-members-table
  (testing "adding a member twice should result in a conflict and return nothing"
    (let [org        (db/create {:name "test11"
                                 :slug "test-111"})
          user       (user-db/create {:email       "dirk@gmail.com"
                                      :given-name  "Dirk"
                                      :family-name "Gently"
                                      :picture     "www.some-url.com"})
          params     {:user-id (:id user)
                      :org-id  (:id org)
                      :role    "admin"}
          response   (db/add-user params)
          response-2 (db/add-user params)]
      (is (nil? response-2))))

  (testing "adding a member with the correct information should return an inserted record"
    (let [org    (db/create {:name "test1423"
                             :slug "test-1423"})
          user   (user-db/create {:email       "dirk@gmail.com"
                                  :given-name  "Dirk"
                                  :family-name "Gently"
                                  :picture     "www.some-url.com"})
          params {:user-id (:id user)
                  :org-id  (:id org)
                  :role    "member"}]
      (is (contains? (db/add-user params) :role))))

  (testing "adding a member without a role should result in an exception"
    (let [org    (db/create {:name "test12"
                             :slug "test-12"})
          user   (user-db/create {:email       "dirk@gmail.com"
                                  :given-name  "Dirk"
                                  :family-name "Gently"
                                  :picture     "www.some-url.com"})
          params {:user-id (:id user)
                  :org-id  (:id org)}]
      (is (thrown? Exception (db/add-user params))))))
