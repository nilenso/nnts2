(ns nnts2.config
  (:require [aero.core :as aero]))

(def ^:private specs (atom nil))

(defn ^:dynamic db-spec []
  (:db-spec @specs))

(defn server-spec []
  (:server-spec @specs))

(defn oauth2-spec []
  (:oauth2-spec @specs))

(defn read
  "Use the provided environment to get it's profile"
  [env]
  (let [config (aero/read-config (clojure.java.io/resource "config/config.edn") {:profile env})]
    (reset! specs config)))

