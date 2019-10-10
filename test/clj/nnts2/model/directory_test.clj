(ns nnts2.model.directory-test
  (:require [clojure.test :refer :all]
            [nnts2.model.directory :as model]
            [nnts2.fixtures :as fixtures]
            [nnts2.data-factory :as factory])
  (:import (java.util UUID)))

(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)

(deftest create-directory-test
  (with-redefs [nnts2.db.directory/create (fn [data] data)]
    (testing "should fail if org doesnt exist"
      (let [dir-data (factory/build-directory "dir-with-fake-org" (UUID/randomUUID))
            dir      (model/create dir-data)]
        (is (string? dir))
        (is (= dir "org doesn't exist"))))))

(deftest create-root-dir-test
  (testing "should add root dir if parent-id is nil and org exists"))

(deftest create-nested-directory-test
  (testing "should add nested dir if orgs exist and match for parent-child directories"
    (let [parent-dir-data (factory/build-directory "parent-dir")
          parent-dir      (model/create parent-dir-data)
          child-dir-data  (factory/build-directory "child-dir" (:org-id parent-dir) (:id parent-dir))
          child-dir       (model/create child-dir-data)]
      (is (map? child-dir))
      (is (:parent-id child-dir) (:id parent-dir)))))

(deftest create-nested-directory-org-conflict-test
  (testing "should give error message if orgs dont match for parent-child directories"
    (let [parent-dir-data     (factory/build-directory "parent-dir")
          parent-dir          (model/create parent-dir-data)
          new-org             (factory/create-org "neworg1" "newslug1")
          child-diff-org-data (factory/build-directory "child-dir-diff-org" new-org (:id parent-dir))
          child-dir-diff-org  (model/create child-diff-org-data)]
      (is (string? child-dir-diff-org)))))

(deftest rows->nested-conversion-test
  (testing "should produce depth 3 map when given 3 nested directory rows"
    (let [row-data   (factory/build-nested-directory-rows 3)
          map-data   (model/directory-rows->nested-directories {} nil row-data)
          req-output {:id          1
                      :name        "dir"
                      :org-id      "orgid"
                      :directories [{:id          2
                                     :name        "dir"
                                     :org-id      "orgid"
                                     :directories [{:id          3
                                                    :name        "dir"
                                                    :org-id      "orgid"
                                                    :directories []}]}]}]
      (is (= 1 (:id map-data)))
      (is (vector? (:directories map-data)))
      (is (= 2 (:id (first (:directories map-data)))))
      (is (vector? (:directories (first (:directories map-data)))))
      (is (= 3 (:id (first (:directories (first (:directories map-data)))))))
      (is (= map-data req-output)))))

(deftest list-nested-directories-test
  (let [org-id      (factory/create-org)
        dir-root    (model/create (factory/build-directory "root" org-id nil))
        dir1-level1 (model/create (factory/build-directory "dir1-level1" org-id (:id dir-root)))
        dir2-level1 (model/create (factory/build-directory "dir2-level1" org-id (:id dir-root)))
        dir3-level2 (model/create (factory/build-directory "dir3-level2" org-id (:id dir1-level1)))]

    (testing "should give root directories only when parent id is nil and show-tree is false/not-given"
      (let [params         {:parent-id nil
                            :org-id    org-id
                            :show-tree false}
            output-notree1 (model/list params)
            output-notree2 (model/list (dissoc params :show-tree))
            output-tree    (model/list (assoc params :show-tree true))]
        (is (= 1 (count output-notree1)))
        (is (= output-notree1 output-notree2))
        (is (= "root" (:name (first output-notree1))))
        (is (empty? (:directories (first output-notree1))))
        (is (= 1 (count output-tree)))
        (is (= 2 (count (:directories (first output-tree)))))))

    (testing "should give children of a directory when that directory id is passed as parent-id"
      (let [params         {:parent-id (:id dir-root)
                            :org-id    org-id
                            :show-tree false}
            output-notree1 (model/list params)
            output-notree2 (model/list (dissoc params :show-tree))
            output-tree    (model/list (assoc params :show-tree true))]
        (is (= 2 (count output-notree1)))
        (is (empty? (:directories (first output-notree1))))
        (is (= output-notree1 output-notree2))
        (is (= 2 (count output-tree)))
        (is (not (empty? (:directories (first output-tree)))))))))
