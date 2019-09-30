(ns nnts2.directory.model-test
  (:require [clojure.test :refer :all]
            [nnts2.model.directory :as directory-model]
            [nnts2.fixtures :as fixtures]
            [nnts2.db.organization :as org-db]
            [nnts2.data-factory :as factory])
  (:import (java.util UUID)))


(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)


(deftest create-directory-test
  (with-redefs [nnts2.db.directory/create (fn [data] data)]
    (testing "should give success=false if org doesnt exist"
      (let [dir-data (factory/directory "dir-with-fake-org" (UUID/randomUUID))
            dir (directory-model/create dir-data)]
        (is (string? dir))
        (is (= dir "org doesn't exist"))))))

(deftest create-nested-directory-test
  (testing "should add nested dir if orgs exist and match for parent-child directories"
    (let [parent-dir-data (factory/directory "parent-dir")
          parent-dir (directory-model/create parent-dir-data)
                                        ;now the child directory creation
          child-dir-data (factory/directory "child-dir" (:org-id parent-dir) (:id parent-dir))
          child-dir (directory-model/create child-dir-data)]
      (is (map? child-dir))
      (is (:parent-id child-dir) (:id parent-dir)))))

(deftest create-nested-directory-org-conflict-test
  (testing "should give error message if orgs dont match for parent-child directories"
    (let [parent-dir-data (factory/directory "parent-dir")
          parent-dir (directory-model/create parent-dir-data)
                                        ;create child directory with another organization
          new-org (factory/create-org "neworg1" "newslug1")
          child-diff-org-data (factory/directory "child-dir-diff-org" new-org (:id parent-dir))
          child-dir-diff-org (directory-model/create child-diff-org-data)]
      (is (string? child-dir-diff-org)))))
