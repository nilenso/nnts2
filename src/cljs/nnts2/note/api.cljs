(ns nnts2.note.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn create-note [data]
  (POST "/note/create"
        {:response-format :json
         :keywords? true
         :params (first data)
         :format :json
         :handler #(do (re-frame/dispatch [:note-submit-success])
                       (js/alert "note created successfully"))}))
