(ns nnts2.user.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

(defn get-info [id db]
  (GET "/user-info"
       {:params          {:id id}
        :response-format :json
        :keywords?       true
        ;:handler         #(assoc db :user-info %)
        :handler         #(re-frame/dispatch [:user-info %])
        }))