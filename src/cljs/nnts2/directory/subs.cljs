(ns nnts2.directory.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::org-directories
 (fn [db [_ org-id]]
   (get-in db [:organization org-id :directories])))

(re-frame/reg-sub
 ::is-selected-directory
 (fn [db [_ dir-id]]
   (and dir-id (= dir-id (:selected-dir db)))))

(re-frame/reg-sub
 ::selected-directory
 (fn [db _]
   (:selected-dir db)))

(re-frame/reg-sub
 ::add-sub-directory
 (fn [db [_ dir-id]]
   (and dir-id (= dir-id (get-in db [:add-sub-directory-form :parent-dir])))))

(re-frame/reg-sub
 ::add-sub-directory-failure
 (fn [db _]
   (get-in db [:add-sub-directory-form :submit-status] )))

#_(re-frame/reg-sub
   ::add-sub-directory-form
   (fn [db _]
     (:add-sub-directory-form db)))
