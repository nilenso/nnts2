(ns nnts2.handler.note-test
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
        (let [request               {:nnts-user   factory/user-id
                                     :body-params {:title "note-title" :content "note-content"}}
              {:keys [status body]} (handler/create request (:id directory))]
          (is (= (assoc
                  (:body-params request)
                  :created-by-id factory/user-id
                  :directory-id (:id directory)) body)))))))

(deftest get-note
  (with-redefs [nnts2.db.note/get (fn [data] data)]
    (testing "should restructure params correctly for db layer"
      (let [directory                     (factory/create-dir "root")
            req-data                      {:nnts-user factory/user-id}
            {:keys [status body headers]} (handler/get-notes req-data (:id directory))]
        (is (= body {:directory-id (:id directory)}))))))
