(ns nnts2.note.handler-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]
            [nnts2.handler.note :as handler])
  (:import (java.util UUID)))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(def user-id (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff"))

(deftest create-note
  (with-redefs [nnts2.db.note/create (fn [data] data)]
    (testing "Should restructure request into params acceptable for db layer"
      (let [request               {:nnts-user   user-id
                                   :body-params {:title "note-title" :content "note-content"}}
            {:keys [status body]} (handler/create request)]
        (is (map? body))
        (is (= status 200))
        (is (= (assoc (:body-params request) :created-by-id user-id) body))))

    (testing "should give 400 when given invalid note data"
      (let [request               {:nnts-user   user-id
                                   :body-params {:title 1234 :content "title is integer"}}
            {:keys [status body]} (handler/create request)]
        (is (= status 400))
        (is (string? body))))

    (testing "should give 400 when given empty note data"
      (let [request               {:nnts-user   user-id
                                   :body-params {:title "" :content ""}}
            {:keys [status body]} (handler/create request)]
        (is (= status 400))
        (is (string? body))))

    (testing "should give 400 when given no user"
      (let [request               {:body-params {:title "no user test" :content "no user test"}}
            {:keys [status body]} (handler/create request)]
        (is (= status 400))
        (is (string? body))))))

(deftest get-note
  (with-redefs [nnts2.db.note/get (fn [data] data)]
    (testing "should restructure params correctly for db layer"
      (let [req-data                      {:nnts-user user-id
                                           :params    {:id           (UUID/randomUUID)
                                                       :title        "title"
                                                       :content      "conetnt"
                                                       :random-param "random-value"}}
            {:keys [status body]} (handler/get-notes req-data)]
        (is (= status 200))
        (is (= (body :created-by-id) user-id))
        (is (map? body))
        (is (contains? body :id))
        (is (contains? body :title))
        (is (contains? body :content))
        (is (not (contains? body :random-param)))))))
