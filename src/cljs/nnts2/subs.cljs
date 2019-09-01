(ns nnts2.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  ::active-panel
  (fn [db _]
    (:active-panel db)))

(re-frame/reg-sub
 :user
 (fn [db _]
   (get-in db [:user :data])))

(re-frame/reg-sub
 :organization
 (fn [db _]
   (get-in db [:organization :data :all])))
