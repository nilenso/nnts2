(ns nnts2.organization.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::show-create-org-form
 (fn [db _]
   (get-in db [:organization :create-org-form :show-form])))

(re-frame/reg-sub
 ::organization
 (fn [db _]
   (get-in db [:organization :orgs])))

(re-frame/reg-sub
 ::create-org-form-failure
 (fn [db _]
   (get-in db [:organization :create-org-form :submit-status])))
