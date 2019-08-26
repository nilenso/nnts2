(ns nnts2.user.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]
            [nnts2.db :as db]))

(enable-console-print!)

(defn get-info []
  (GET "/user-info"
       {:response-format :json
        :keywords?       true
        :handler         #(re-frame/dispatch [:user-info %])}))
