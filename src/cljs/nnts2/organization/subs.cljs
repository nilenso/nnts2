(ns nnts2.organization.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::show-create-org-form
 (fn [db _]
   (get-in db [:organization :show-create-org-form])))

(re-frame/reg-sub
 ::organization
 (fn [db _]
   (get-in db [:organization :data :all])))
