(ns nnts2.note.functional-test
  (:require  [clojure.test :refer :all]
             [nnts2.fixtures :as fixtures]
             [nnts2.note.handler :as api]
             [nnts2.note.db :as db]))


(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)


(def request {:session {:user-info {:nnts-id 1}}
              :body {:title "note-title" :content "note-content"}})


(deftest create-note
  (testing "Note create success"
    (let [response (api/create request)
          input (assoc
                 (:body request)
                 :created-by-id
                 (get-in request [:session :user-info :nnts-id]))
          output (select-keys (:body response) [:title :content :created-by-id])]
      (is (= (:status response) 200))
      (is (= input output))))

  (testing "Note spec failure"
    (let [request-mod (assoc-in request [:body :title] 12334)
          response (api/create request-mod)
          input (assoc
                 (:body request-mod)
                 :created-by-id
                 (get-in request-mod [:session :user-info :nnts-id]))]
      (is (= (:status response) 400))
      (is (string? (:body response))))))



(deftest get-notes
  (testing "creation of one note should give one note on get"
    (let [create-response (api/create request)
          get-request (dissoc request :body)
          get-response (api/getnotes request)
          note-body-request (assoc (:body request) :created-by-id (get-in request [:session :user-info :nnts-id]))
          note-body-response (select-keys (first (:body get-response)) [:title :content :created-by-id])]
      (is (= (:status get-response) 200))
      (is (= (count (:body get-response)) 1))
      (is (= note-body-request note-body-response))))

  (testing "getting note for a different user should generate empty result"
    (let [new-request (assoc-in request [:session :user-info :nnts-id] 2)
          get-response (api/getnotes new-request)]
      (is (= (:status get-response) 200))
      (is (= (count (:body get-response)) 0))))

  (testing "getting 2 notes if 2 notes hv been created"
    (let [response (api/create request)
          get-response (api/getnotes request)]
      (is (= (:status get-response) 200))
      (is (= (count (:body get-response)) 2)))))
