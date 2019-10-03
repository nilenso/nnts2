(ns nnts2.directory.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::org-directories
 (fn [db [_ org-id]]
   (get-in db [:organization org-id :directories])))

(re-frame/reg-sub
 ::selected-directory
 (fn [db [_ dir-id]]
   (if dir-id
     (if (= dir-id (:selected-dir db)) true false)
     false)))
