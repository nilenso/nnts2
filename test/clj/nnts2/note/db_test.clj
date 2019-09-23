(ns nnts2.note.db-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]
            [nnts2.db.note :as note-db])
  (:import (java.util UUID)))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(def user-id (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff"))

(deftest create-note-test
  (testing "should return note map when given correct params"
    (let [fake-note {:title "note-title"
                     :content "note-content"
                     :created-by-id user-id}
          output (note-db/create fake-note)]
      (is (map? output))
      (is (= fake-note (select-keys output [:title :content :created-by-id])))
      (is (contains? output :id))
      (is (contains? output :created-at))
      (is (contains? output :note-time))
      (is (contains? output :updated-at)))))


(deftest get-note-test
  (testing "should return one note if one note has been created"
    (let [fake-note {:title "note-title"
                     :content "note-content"
                     :created-by-id user-id}
          created-note (note-db/create fake-note)
          get-req-body {:created-by-id user-id}
          get-req-output (note-db/get get-req-body)]
      (is (= (count get-req-output) 1))
      (is (first get-req-output) created-note)))

  (testing "should return two notes if two have been created"
    (let [fake-note2 {:title "note-title2"
                      :content "note-content2"
                      :created-by-id user-id}
          created-note2 (note-db/create fake-note2)
          get-req-body {:created-by-id user-id}
          get-req-output (note-db/get get-req-body)]
      (is (= (count get-req-output) 2))
      (is (last get-req-output) created-note2)))

  (testing "Should throw postgres exception  without existing nnts-user"
    (let [request {:body {:title "non-existent user"
                          :content "non-existent user"
                          :created-by-id (UUID/randomUUID)}}]
      (is (thrown? org.postgresql.util.PSQLException (note-db/create request))))))


(deftest get-note-with-params-test
  (testing "should get note with id if it belongs to an existing note"
    (let [fake-note {:title "note-title"
                     :content "note-content"
                     :created-by-id user-id}
          create-output (note-db/create fake-note)
          create-output2 (note-db/create fake-note)
          get-req-body {:created-by-id user-id
                        :id (:id create-output)}
          get-req-output (note-db/get get-req-body)]
      (is (= (count get-req-output) 1))))

  (testing "should get no notes for a particular id if it doesnt exist"
    (let [get-req-body {:created-by-id user-id
                        :id (UUID/randomUUID)}
          get-req-output (note-db/get get-req-body)]
      (is (empty? get-req-output))))

  (testing "should give no notes for a user who has not created any notes"
    (let [get-req-body {:created-by-id (UUID/randomUUID)}
          get-req-output (note-db/get get-req-body)]
      (is (empty? get-req-output)))))
