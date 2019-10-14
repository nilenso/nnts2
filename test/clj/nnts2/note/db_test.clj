(ns nnts2.note.db-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]
            [nnts2.data-factory :as factory]
            [nnts2.db.note :as note-db]
            [nnts2.routes.note :as note]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(deftest create-note-test
  (testing "should return note map when given correct params"
    (let [directory (factory/create-dir "root")
          note      (factory/note (:id directory))
          output    (note-db/create note)]
      (is (map? output))
      (is (= note (select-keys output [:title :content :created-by-id :directory-id])))
      (is (contains? output :id))
      (is (contains? output :created-at))
      (is (contains? output :note-time))
      (is (contains? output :updated-at)))))

(deftest get-note-test
  (let [directory (factory/create-dir "root")]
    (testing "should return one note if one note has been created"
      (let [note           (factory/note (:id directory))
            created-note   (note-db/create note)
            get-req-body   {:directory-id (:id directory)}
            get-req-output (note-db/get get-req-body)]
        (is (= (count get-req-output) 1))
        (is (first get-req-output) created-note)))

    (testing "should return two notes if two have been created"
      (let [note2          (factory/note (:id directory) "note2" "content2")
            created-note2  (note-db/create note2)
            get-req-body   {:directory-id  (:id directory)
                            :created-by-id factory/user-id}
            get-req-output (note-db/get get-req-body)]
        (is (= (count get-req-output) 2))
        (is (last get-req-output) created-note2)))

    (testing "should return only one note if second note is created in another directory"
      (let [directory2         (factory/create-dir "subdir" (:org-id directory) (:id directory))
            note3              (factory/note (:id directory2) "note3" "content3")
            create-note3       (note-db/create note3)
            get-notes-root-dir (note-db/get {:directory-id (:id directory)})
            get-notes-sub-dir  (note-db/get {:directory-id (:id directory2)})]
        (is (= 2 (count get-notes-root-dir)))
        (is (= 1 (count get-notes-sub-dir)))))

    (testing "Should throw postgres exception  without existing nnts-user"
      (let [note (factory/note (:id directory) "non existent user" "non existent user" (factory/get-uuid))]
        (is (thrown? org.postgresql.util.PSQLException (note-db/create note)))))))

(deftest get-note-with-params-test
  (let [directory (factory/create-dir "root")
        note      (factory/note (:id directory))]
    (testing "should get note with id if it belongs to an existing note"
      (let [create-output  (note-db/create note)
            create-output2 (note-db/create note)
            get-req-body   {:created-by-id factory/user-id
                            :id            (:id create-output)}
            get-req-output (note-db/get get-req-body)]
        (is (= (count get-req-output) 1))))

    (testing "should get no notes for a particular id if it doesnt exist"
      (let [get-req-body   {:created-by-id factory/user-id
                            :id            (factory/get-uuid)}
            get-req-output (note-db/get get-req-body)]
        (is (empty? get-req-output))))

    (testing "should give no notes for a user who has not created any notes"
      (let [get-req-body   {:created-by-id (factory/get-uuid)}
            get-req-output (note-db/get get-req-body)]
        (is (empty? get-req-output))))))
