(ns nnts2.spec-helpers
  (:require [ring.util.response :as res]))

(defn invalid [str]
  (-> (res/response str)
      (res/status 400)))
