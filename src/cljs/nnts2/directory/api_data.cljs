(ns nnts2.directory.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))


(defn create-directory [data]
  {:method :post
   :uri (goog.string/format "/org/%s/dir" (:org-id data))
   :response-format (ajax/json-response-format {:keywords? true})
   :params (dissoc data :org-id)
   :format (ajax/json-request-format)
   :on-success [:nnts2.directory.events/create-directory-success]})


(defn get-directories [data]
  {:method :get
   :uri (goog.string/format "/org/%s/dir/?recursive=true" data)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success [:nnts2.directory.events/received-directory-list]})
