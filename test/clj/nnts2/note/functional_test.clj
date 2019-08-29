(ns nnts2.note.functional-test
  (:require  [clojure.test :refer :all]
             [nnts2.fixtures :as fixtures]
             [nnts2.note.handler :as api]
             [nnts2.note.db :as db]))


(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)


(deftest create-note-success
  (testing "String title, content and valid user id -> note success"
    (let [request {:nnts-user 1 :body {:title "note-title" :content "note-content"}}
          {:keys [body status]} (api/create request)
          input (into (:body request) {:created-by-id 1})]
      (is (= status 200))
      (is (= input (select-keys body [:title :content :created-by-id]))))))

(deftest create-note-failure
  (testing "Given integer title, receive 400 error with string explanation "
    (let [request {:nnts-user 1 :body {:title 12344 :content "note-content"}}
          {:keys [body status]} (api/create request)]
      (is (= status 400))
      (is (string? body)))))


(deftest get-notes
  (testing "creation of one note should give one note on get"
    (let [request {:nnts-user 1 :body {:title "note-title" :content "note-content"}}
          resp (api/create request)
          get-request (dissoc request :body)
          {:keys [body status]} (api/getnotes request)
          note-body-request (into (:body request) {:created-by-id 1})]
      (is (= status 200))
      (is (= (count body) 1))
      (is (= note-body-request (select-keys (first body) [:title :content :created-by-id])))))

  (testing "getting note for a different user should generate empty result"
    (let [request {:nnts-user 2}
          {:keys [body status]} (api/getnotes request)]
      (is (= status 200))
      (is (= (count body) 0))))

  (testing "getting 2 notes if 2 notes hv been created"
    (let [request {:nnts-user 1 :body {:title "new-note-title" :content "new-note-content"}}
          response (api/create request)
          get-response (api/getnotes request)]
      (is (= (:status get-response) 200))
      (is (= (count (:body get-response)) 2)))))
