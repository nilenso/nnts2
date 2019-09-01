(ns nnts2.user.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

(defn get-info []
  (GET "/user-info"
       {:response-format :json
        :format          :json
        :keywords?       true
        :handler         (fn [response]
                           (re-frame/dispatch [:user-info response])
                           (re-frame/dispatch [:get-orgs]))}))
