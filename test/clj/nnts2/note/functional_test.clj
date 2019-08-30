(ns nnts2.note.functional-test
  (:require  [clojure.test :refer :all]
             [nnts2.fixtures :as fixtures]
             [nnts2.handler.note :refer [create]]
             [nnts2.db.note :refer [add]]))


(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)


(deftest create-note-success
  (testing "String title, content and valid user id -> note success"
    (let [request {:nnts-user 1 :body {:title "note-title" :content "note-content"}}
          {:keys [body status]} (create request)
          input (into (:body request) {:created-by-id 1})]
      (is (= status 200))
      (is (= input (select-keys body [:title :content :created-by-id]))))))

(deftest create-note-failure
  (testing "Given integer title, receive 400 error with string explanation "
    (let [request {:nnts-user 1 :body {:title 12344 :content "note-content"}}
          {:keys [body status]} (create request)]
      (is (= status 400))
      (is (string? body)))))
