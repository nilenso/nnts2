(ns nnts2.utils.http-utils
  (:require [ring.util.response :as res]))

(defn create-response [data]
  (condp #(contains? %2 %1) data
    :error   (-> (res/response (str (:error data)))
                 (res/status 400))
    :message (-> (res/response (str (:message data)))
                 (res/status 200))
    (-> (res/response data)
        (res/status 200))))
