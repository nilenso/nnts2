(ns nnts2.db.directory-test
  (:require [clojure.test :refer :all]
            [nnts2.db.directory :as db]
            [nnts2.fixtures :as fixtures]
            [nnts2.data-factory :as factory]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(deftest create-dir-test
  (testing "should create dir in org when given existing user, org and dir name"
    (let [dir-data (factory/build-directory "dir")
          output   (db/create dir-data)]
      (is (not (empty? output)))
      (is (= dir-data (select-keys output [:name :parent-id :org-id :created-by-id]))))))

(deftest create-nested-dir-test
  (testing "should create sub directory in directory when given name, existing directory,userwith correct org"
    (let [dir-data     (factory/build-directory "dir")
          dir          (db/create dir-data)
          sub-dir-data (factory/build-directory "subdir" (:org-id dir) (:id dir))
          sub-dir      (db/create sub-dir-data)]
      (is (not (empty? sub-dir)))
      (is (= (:parent-id sub-dir) (:id dir)))
      (is (= sub-dir-data (select-keys sub-dir [:name :parent-id :org-id :created-by-id]))))))

(deftest get-dir-test
  (testing "should give one root dir when only one root dir has been created"
    (let [dir-data              (factory/build-directory "dir")
          dir                   (db/create dir-data)
          get-dir               (db/get)
          get-dir-for-org       (db/get {:org-id (:org-id dir)})
          get-dir-for-other-org (db/get {:org-id (factory/create-org "org" "slug")})]
      (is (= 1 (count get-dir)))
      (is (= 1 (count get-dir-for-org)))
      (is (empty? get-dir-for-other-org)))))

(deftest get-subdir-test
  (testing "should give correct nested dir rows when parent dir is given"
    (let [dir-data      (factory/build-directory "dir")
          dir           (db/create dir-data)
          sub-dir-data  (factory/build-directory "subdir" (:org-id dir) (:id dir))
          sub-dir       (db/create sub-dir-data)
          sub-dir2-data (factory/build-directory "sub2" (:org-id sub-dir) (:id sub-dir))
          sub-dir2      (db/create sub-dir2-data)
          get-output    (db/get)]
      (is (= 3 (count get-output)))
      (is (= (:org-id dir) (first (set (map :org-id get-output)))))
      (is (nil? (:parent-id dir)))
      (is (= (:parent-id sub-dir) (:id dir)))
      (is (= (:parent-id sub-dir2) (:id sub-dir))))))

(deftest create-dir-test-failure
  (testing "should give error when name is duplicate in the same parent directory"))
