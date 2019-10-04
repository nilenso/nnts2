(ns nnts2.note.handler-test
  (:require [clojure.test :refer :all]
            [nnts2.fixtures :as fixtures]
            [nnts2.handler.note :as handler]
            [nnts2.data-factory :as factory]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)


(deftest create-note
  (with-redefs [nnts2.db.note/create (fn [data] data)]
    (let [directory (factory/create-dir "root")]
      (testing "Should restructure request into params acceptable for db layer"
        (let [fake-request {:nnts-user factory/user-id
                            :body-params      {:title "note-title" :content "note-content"}}
              {:keys [status body]} (handler/create fake-request (:id directory))]
          (is (map? body))
          (is (= status 200))
          (is (= (assoc
                  (:body-params fake-request)
                  :created-by-id factory/user-id
                  :directory-id (:id directory)) body))))

      (testing "should give 400 when given invalid note data"
        (let [fake-request {:nnts-user factory/user-id
                            :body-params {:title 1234 :content "title is integer"}}
              {:keys [status body]} (handler/create fake-request (:id directory))]
          (is (= status 400))
          (is (string? body))))

      (testing "should give 400 when given empty note data"
        (let [fake-request {:nnts-user factory/user-id
                            :body-params {:title "" :content ""}}
              {:keys [status body]} (handler/create fake-request (:id directory))]
          (is (= status 400))
          (is (string? body))))

      (testing "should give 400 when given no user"
        (let [fake-request {:body-params {:title "no user test" :content "no user test"}}
              {:keys [status body]} (handler/create fake-request (:id directory))]
          (is (= status 400))
          (is (string? body)))))))


(deftest get-note
  (with-redefs [nnts2.db.note/get (fn [data] data)]
    (testing "should restructure params correctly for db layer"
      (let [directory (factory/create-dir "root")
            fake-req-data {:nnts-user factory/user-id}
            {:keys [status body headers]} (handler/get-notes fake-req-data (:id directory))]
        (is (= status 200))
        (is (= (body :directory-id) (:id directory)))
        (is (= (body :created-by-id) factory/user-id))
        (is (map? body))))))
