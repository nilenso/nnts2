(ns nnts2.handler.directory-test
  (:require [clojure.test :refer :all]
            [nnts2.handler.directory :as handler]
            [nnts2.fixtures :as fixtures]
            [nnts2.data-factory :as factory]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(deftest create-dir
  (with-redefs [nnts2.model.directory/create (fn [data] (assoc data :id (factory/get-uuid)))]
    (testing "should restructure request by adding org-id and created-by-id from request"
      (let [org-id                (factory/get-uuid)
            request               {:nnts-user factory/user-id}
            body-                 {:name "directory" :parent-id nil}
            {:keys [body status]} (handler/create request org-id body-)]
        (is (= status 200))
        (is (map? body))
        (is (= body (assoc body :org-id org-id :created-by-id factory/user-id)))))))

(deftest list-dir
  (with-redefs [nnts2.model.directory/list (fn [data] data)]
    (testing "should restructure input arguments correctly"
      (let [org-id                (factory/get-uuid)
            request               {:nnts-user factory/user-id}
            parent-id             (factory/get-uuid)
            show-tree             true
            {:keys [body status]} (handler/list request org-id parent-id show-tree)]
        (is (= body {:parent-id parent-id :show-tree show-tree :org-id org-id}))))))
