(ns nnts2.note.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::note-form
 (fn [db _]
   (prn "subscribe to form" (:note-form db) _)
   (:note-form db))
 )
