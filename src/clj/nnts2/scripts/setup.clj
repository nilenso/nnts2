(ns nnts2.scripts.setup
  (:require [nnts2.scripts.migrate-notes-without-directories :as migrate-note]))

(defn call-script [script-name & args]
  (cond
    (= script-name "migrate-notes-without-directories") (migrate-note/run args)
    :else                                               "Not a valid script name"))
