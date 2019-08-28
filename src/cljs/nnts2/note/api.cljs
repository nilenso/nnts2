(ns nnts2.note.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn create-note [data]
  (prn data)
  (POST "/note/create"
        {:response-format :json
         :keywords? true
         :params data
         :handler #(re-frame/dispatch [:note-get-list])}
        ))
