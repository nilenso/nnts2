(ns nnts2.note.api
  (:require [ajax.core :refer [GET POST]]
            [re-frame.core :as re-frame]))

(enable-console-print!)

(defn create-note [data]
  (prn "why the fuck is it getting to post" data)
  #_(GET "/note/create"
        {:response-format :json
         ;:keywords? true
         :params {:title "some" :content "thing"}
         ;:handler #(re-frame/dispatch [:note-get-list])
         }
        ))
