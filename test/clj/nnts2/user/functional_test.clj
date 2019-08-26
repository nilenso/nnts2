(ns nnts2.user.functional-test
  (:require [clojure.test :refer :all]
            [clojure.set :as set]
            [nnts2.fixtures :refer [clear setup]]
            [nnts2.user.handler :refer [create]]
            [nnts2.user.db :refer [get-by-email]]))

(use-fixtures :each clear)
(use-fixtures :once setup)

(defn correct-output [output]
  (-> output
      (dissoc :id)
      (set/rename-keys {:image-url  :picture
                        :first-name :given-name
                        :last-name  :family-name})))

(def session {:session {:user-info {:email       "dirk@gmail.com"
                                    :given-name  "Dirk"
                                    :family-name "Gently"
                                    :picture     "www.some-url.com"}}})

(deftest create-user
  (testing "Given correct user name, new email -> add user"
    (let [response (create session)
          input (get-in session [:session :user-info])
          output (correct-output response)]
      (is (= (:status output) 302))))

  (testing "Edit user"
    (let [response-1 (create session)
          new-given-name "Ford"
          response-2 (create (assoc-in session [:session :user-info :given-name] new-given-name))
          corrected-output (correct-output response-2)]
       (is (= (:status corrected-output) 302)))))
