(ns nnts2.note.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::note-form
 (fn [db _]
   (:note-form db))
 )
