(ns nnts2.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :active-panel
  (fn [db _]
    (:active-panel db)))

(re-frame/reg-sub
  :user-info
  (fn [db _]
    (:user-info db)))