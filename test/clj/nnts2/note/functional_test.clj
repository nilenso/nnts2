(ns nnts2.note.functional-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]
            [nnts2.handler.note :refer [create]]
            [nnts2.db.note :refer [add]])
  (:import (org.postgresql.util PSQLException)
           (java.util UUID)))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(deftest create-note-success
  (testing "Should create note successfully for valid params"
    (let [request {:nnts-user (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff")
                   :body      {:title "note-title" :content "note-content"}}
          {:keys [body status]} (create request)
          input (into (:body request) {:created-by-id (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff")})]
      (is (= status 200))
      (is (= input (select-keys body [:title :content :created-by-id]))))))

(deftest create-note-failure
  (testing "Should receive 400 error with string explanation for an integer title"
    (let [request {:nnts-user (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff")
                   :body {:title 12344 :content "note-content"}}
          {:keys [body status]} (create request)]
      (is (= status 400))
      (is (string? body))))
  (testing "Should receive 400 error for missing title"
    (let [request {:nnts-user (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff")
                   :body {:content "only-content"}}
          {:keys [status body]} (create request)]
      (is (= status 400))
      (is (string? body))))
  (testing "Should receive 400 error for missing content"
    (let [request {:nnts-user (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff")
                   :body {:title "only-titlet"}}
          {:keys [status body]} (create request)]
      (is (= status 400))
      (is (string? body))))
  (testing "Should throw postgres exception  without nnts-user"
    (let [request {:body {:title "no user" :content "no user"}}]
      (is (thrown? PSQLException (create request)))))
  (testing "Should throw postgres exception  without existing nnts-user"
    (let [request {:nnts-user (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff")
                   :body {:title "non-existent user" :content "non-existent user"}}]
      (is (thrown? PSQLException (create request))))))
