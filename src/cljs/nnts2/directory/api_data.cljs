(ns nnts2.directory.api-data
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]))

(defn create-directory [data]
  {:method          :post
   :uri             (str "/orgs/" (:org-id data) "/dirs")
   :response-format (ajax/json-response-format {:keywords? true})
   :params          (dissoc data :org-id)
   :format          (ajax/json-request-format)
   :on-success      [:nnts2.directory.events/create-directory-success]
   :on-failure      [:nnts2.directory.events/create-directory-failure]})

(defn get-directories [org-id]
  {:method          :get
   :uri             (str "/orgs/" org-id  "/dirs/?show-tree=true")
   :response-format (ajax/json-response-format {:keywords? true})
   :on-success      [:nnts2.directory.events/received-directory-list]})
