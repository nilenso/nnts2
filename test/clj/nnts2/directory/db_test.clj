(ns nnts2.directory.db-test
  (:require [clojure.test :refer :all]
            [nnts2.db.directory :as directory-db]
            [nnts2.fixtures :as fixtures]
            [nnts2.data-factory :as factory]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(deftest create-dir-test
  (testing "should create dir in org when given existing user, org and dir name"
    (let [fake-dir-data  (factory/directory "fake-dir")
          output (directory-db/create fake-dir-data)]
      (is (not (empty? output)))
      (is (= fake-dir-data(select-keys output [:name :parent-id :org-id :created-by-id]))))))


(deftest create-nested-dir-test
  (testing "should create sub directory in directory when given name, existing directory,userwith correct org"
    (let [fake-dir-data (factory/directory "fakedir")
          fake-dir (directory-db/create fake-dir-data)
          fake-sub-dir-data (factory/directory "fakesubdir" (:org-id fake-dir) (:id fake-dir))
          fake-sub-dir (directory-db/create fake-sub-dir-data)]
      (is (not (empty? fake-sub-dir)))
      (is (= (:parent-id fake-sub-dir) (:id fake-dir)))
      (is (= fake-sub-dir-data (select-keys fake-sub-dir [:name :parent-id :org-id :created-by-id]))))))


(deftest get-dir-test
  (testing "should give one root dir when only one root dir has been created"
    (let [fake-dir-data (factory/directory "fakedir")
          fake-dir (directory-db/create fake-dir-data)
          get-dir (directory-db/get)
          get-dir-for-org (directory-db/get {:org-id (:org-id fake-dir)})
          get-dir-for-other-org (directory-db/get {:org-id (factory/create-org "org" "slug")})]
      (is (= 1 (count get-dir)))
      (is (= 1 (count get-dir-for-org)))
      (is (empty? get-dir-for-other-org)))))


(deftest get-subdir-test
  (testing "should give correct nested dir rows when parent dir is given"
    (let [fake-dir-data (factory/directory "fakedir")
          fake-dir (directory-db/create fake-dir-data)
          sub-dir-data (factory/directory "subdir" (:org-id fake-dir) (:id fake-dir))
          sub-dir (directory-db/create sub-dir-data)
          sub-dir2-data (factory/directory "sub2" (:org-id sub-dir) (:id sub-dir))
          sub-dir2 (directory-db/create sub-dir2-data)
          get-output (directory-db/get)]
      (is (= 3 (count get-output)))
      (is (= (:org-id fake-dir) (first (set (map :org-id get-output)))))
      (is (nil? (:parent-id fake-dir)))
      (is (= (:parent-id sub-dir) (:id fake-dir)))
      (is (= (:parent-id sub-dir2) (:id sub-dir))))))


(deftest create-dir-test-failure
  (testing "should give error when name is duplicate in the same parent directory"))
