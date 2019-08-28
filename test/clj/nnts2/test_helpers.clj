(ns nnts2.test-helpers
  (:require  [clojure.test :as t]
             [clojure.string :as str]
             [org.httpkit.client :as http]
             [cheshire.core :as json]))

(defn settings
  [setting]
  (let [port 8000
        host (str/join ["http://localhost:" port "/"])
        base-url host]
    (setting {:port port :host host :base-url base-url})))

(defn http-request-raw
  ([method url] (http-request-raw method url nil))
  ([method url body]
   (let [params   (merge {:url url
                          :method method
                          :headers  {"authorization" (first (str/split-lines (slurp "/Users/murtaza/Dev/nnts2/test/clj/nnts2/token")))
                                     "Content-Type" "application/json"}
                          :as :text}
                         (if body {:body (json/encode body)}))
         response @(http/request params)]
     response)))

(defn http-request
  [& args]
  (let [response (apply http-request-raw args)]
    (assoc response :body (json/decode (:body response)))))
