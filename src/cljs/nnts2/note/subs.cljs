(ns nnts2.note.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::notes
 (fn [db _]
   (:notes db)))

(re-frame/reg-sub
 ::note-form-data
 (fn [db _]
   (:data (:note-form db))))

(re-frame/reg-sub
 ::note-form-submit-error
 (fn [db _]
   (:submit-status (:note-form db))))
