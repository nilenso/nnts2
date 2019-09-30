(ns nnts2.directory.db-test
  (:require [clojure.test :refer :all]
            [nnts2.db.directory :as directory-db]
            [nnts2.fixtures :as fixtures]))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(def user-id (UUID/fromString "820bb852-445f-4101-b257-f84f66aa74ff"))


(deftest create-root-dir-test
  (testing "should create dir in org when given existing user, org and dir name"))


(deftest create-nested-dir-test
  (testing "should create sub directory in directory when given name, existing directory,user with correct org"))

(deftest get-dir-test
  (testing "should give one root dir when only one root dir has been created"))

(deftest get-subdir-test
  (testing "should give correct nested dir structure when parent dir is given"))
