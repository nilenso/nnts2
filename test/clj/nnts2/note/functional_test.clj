(ns nnts2.note.functional-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]
            [nnts2.handler.note :as handler]
            [nnts2.db :as db])
  (:import (org.postgresql.util PSQLException)
           (java.util UUID)))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(def user-id (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff"))

(deftest create-note-success
  (testing "Should create note successfully for valid params"
    (let [request {:nnts-user user-id
                   :body      {:title "note-title" :content "note-content"}}
          {:keys [body status]} (handler/create request)
          input (into (:body request) {:created-by-id user-id})]

      (is (= status 200))
      (is (= input (select-keys body [:title :content :created-by-id]))))))


(deftest create-note-failure
  (testing "Should receive 400 error with string explanation for an integer title"
    (let [request {:nnts-user user-id
                   :body {:title 12344 :content "note-content"}}
          {:keys [body status]} (handler/create request)]
      (is (= status 400))
      (is (string? body))))

  (testing "Should receive 400 error for missing title"
    (let [request {:nnts-user user-id
                   :body {:content "only-content"}}
          {:keys [status body]} (handler/create request)]
      (is (= status 400))
      (is (string? body))))

  (testing "Should receive 400 error for missing content"
    (let [request {:nnts-user user-id
                   :body {:title "only-titlet"}}
          {:keys [status body]} (handler/create request)]
      (is (= status 400))
      (is (string? body))))

  (testing "Should receive 400 error  without nnts-user"
    (let [request {:body {:title "no user test" :content "no user"}}
          {:keys [status body]} (handler/create request)]
      (is (= status 400))
      (is (string? body))))

  (testing "Should throw postgres exception  without existing nnts-user"
    (let [request {:nnts-user (UUID/randomUUID)
                   :body {:title "non-existent user" :content "non-existent user"}}]
      (is (thrown?  org.postgresql.util.PSQLException (handler/create request))))))



(deftest get-notes
  (testing "creation of one note should give one note on get"
    (let [fake-note {:nnts-user user-id
                     :body {:title "note-title" :content "note-content"}}
          resp (handler/create fake-note)
          get-request-data {:nnts-user user-id}
          {:keys [body status]} (handler/get-notes get-request-data)
          note-body-request (into (:body fake-note) {:created-by-id user-id})]
      (is (= status 200))
      (is (= (count body) 1))
      (is (= note-body-request (select-keys (first body) [:title :content :created-by-id])))))

  (testing "getting note for a different user should generate empty result"
    (let [request {:nnts-user (UUID/randomUUID)}
          {:keys [body status]} (handler/get-notes request)]
      (is (= status 200))
      (is (= (count body) 0))))

  (testing "getting 2 notes if 2 notes hv been created"
    (let [request {:nnts-user user-id
                   :body {:title "new-note-title" :content "new-note-content"}}
          response (handler/create request)
          {:keys [body status]} (handler/get-notes request)]
      (is (= status 200))
      (is (= (count body) 2)))))
