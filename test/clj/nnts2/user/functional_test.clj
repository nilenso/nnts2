(ns nnts2.user.functional-test
  (:require [clojure.test :refer :all]
            [clojure.set :as set]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.user.handler :refer [create]]
            [nnts2.user.db :refer [get-by-email]]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(def session {:session {:user-info {:email       "dirk@gmail.com"
                                    :given-name  "Dirk"
                                    :family-name "Gently"
                                    :picture     "www.some-url.com"}}})

(deftest create-user
  (testing "Given correct user name, new email -> add user"
    (let [response (create session)
          input (get-in session [:session :user-info])
          db-row (dissoc (get-by-email (:email input)) :id)]
      (is (= (:status response) 302))
      (is (= (vals db-row) (vals input)))))

  (testing "Edit user"
    (let [new-given-name "Ford"
          new-session (assoc-in session [:session :user-info :given-name] new-given-name)
          new-input (get-in new-session [:session :user-info])
          old-db-row (get-by-email (:email new-input))
          response (create new-session)
          new-db-row (get-by-email (:email new-input))]
      (is (= (:status response) 302))
      (is (= (:id old-db-row) (:id new-db-row)))
      (is (= (vals (dissoc new-db-row :id)) (vals new-input))))))
