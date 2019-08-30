(ns nnts2.user.functional-test
  (:require [clojure.test :refer :all]
            [clojure.set :as set]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.handler.user :refer [create]]
            [nnts2.db.user :refer [get-by-email]]))

(use-fixtures :each clear)
(use-fixtures :once setup)



(def request {:google-user {:email       "dirk@gmail.com"
                            :given-name  "Dirk"
                            :family-name "Gently"
                            :picture     "www.some-url.com"}})

(deftest create-user
  (testing "Given correct user name, new email -> add user"
    (let [{:keys [status]} (create request)
          input (get-in request [:google-user])
          db-row (dissoc (get-by-email (:email input)) :id)]
      (is (= status 302))
      (is (= (vals db-row) (vals input)))))

  (testing "Edit user"
    (let [new-req {:google-user
                   {:email "dirk@gmail.com"
                    :given-name "Ford"
                    :family-name "Gently"
                    :picture "www.some-other-url.com"}}
          old-db-row (get-by-email "dirk@gmail.com")
          {:keys [status]} (create new-req)
          new-db-row (get-by-email "dirk@gmail.com")]
      (is (= status 302))
      (is (= (:id old-db-row) (:id new-db-row)))
      (is (= (:first-name new-db-row) "Ford")))))
