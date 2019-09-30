(ns nnts2.directory.handler-test
  (:require [clojure.test :refer :all]
            [nnts2.model.directory :as directory-model]
            [nnts2.db.organization :as org-db]
            [nnts2.fixtures :as fixtures]))




(use-fixtures :each fixtures/clear)
(use-fixtures :once fixtures/setup fixtures/adduser)
