(ns nnts2.directory.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))

(defn create-directory [data]
  {:method :post
   :uri (goog.string/format "/orgs/%s/dirs" (:org-id data))
   :response-format (ajax/json-response-format {:keywords? true})
   :params (dissoc data :org-id)
   :format (ajax/json-request-format)
   :on-success [:nnts2.directory.events/create-directory-success]})

(defn get-directories [org-id]
  {:method :get
   :uri (goog.string/format "/orgs/%s/dirs/?show-tree=true" org-id)
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success [:nnts2.directory.events/received-directory-list]})
