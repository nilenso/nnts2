(ns nnts2.user.handler
  (:require [ring.util.response :as res]
            [clj-http.client :refer [with-middleware get default-middleware]]
            [nnts2.user.db :as db]
            [cheshire.core :as json]))

(defn add [request]
  (prn request)
  #_(db/add () info))
