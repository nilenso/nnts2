(ns nnts2.scripts.setup
  (:require [nnts2.scripts.migrate-notes-without-directories :as migrate-note]))

(defn call-script [script-name & args]
  (cond
    (= script-name "migrate-notes-without-directories")
    (do
      (prn (migrate-note/run args))
      (prn "directories added to note"))
    :else "Not a valid script name"))
