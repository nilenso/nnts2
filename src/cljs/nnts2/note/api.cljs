(ns nnts2.note.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn create-note [data]
  (prn "Creat enote called")
  (POST "/note/create"
        {:response-format :json
         :keywords? true
         :params (first data)
         :format :json
         :handler #(re-frame/dispatch [:note-get-list])}))

(defn get-notes []
  (GET "/note/get"
       {:response-format :json
        :keywords? true
        :handler #(re-frame/dispatch [:note-received-list %])}))
