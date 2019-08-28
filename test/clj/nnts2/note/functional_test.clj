(ns nnts2.note.functional-test
  (:require  [clojure.test :refer :all]
             [nnts2.fixtures :as fixtures]
             [nnts2.note.handler :refer [create]]
             [nnts2.note.db :refer [add]]))


(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)


(def request {:session {:user-info {:nnts-id 1}}
              :body {:title "note-title" :content "note-content"}})


(deftest create-note
  (testing "Note create success"
    (let [response (create request)
          input (assoc
                 (:body request)
                 :created-by-id
                 (get-in request [:session :user-info :nnts-id]))
          output (select-keys (:body response) [:title :content :created-by-id])]
      (is (= (:status response) 200))
      (is (= input output))))

  (testing "Note spec failure"
    (let [request-mod (assoc-in request [:body :title] 12334)
          response (create request-mod)
          input (assoc
                 (:body request-mod)
                 :created-by-id
                 (get-in request-mod [:session :user-info :nnts-id]))]
      (is (= (:status response) 400))
      (is (string? (:body response))))))
