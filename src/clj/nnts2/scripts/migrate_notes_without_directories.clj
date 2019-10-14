(ns nnts2.scripts.migrate-notes-without-directories
  (:require [nnts2.config :as config]
            [nnts2.model.organization :as org-model]
            [nnts2.model.directory :as dir-model]
            [nnts2.model.note :as note-model]
            [nnts2.db.note :as note-db]))

                                        ;All users will have a "Home" org. with a general channel.
                                        ;all notes without directories would be moved to the above general channel

(defn get-notes-wo-directories []
  (note-db/get {:directory-id nil}))


(defn create-general-directory-under-private-org-for-user [user-id]
  (let [{org-id :id}         (org-model/create-org-add-membership
                              {:name "MyNotes" :slug (str "MyNotes-" user-id)}
                              user-id)
        dir-data             {:org-id org-id :name "general" :parent-id nil :created-by-id user-id}
        existing-general-dir (first (dir-model/list dir-data))]

    {:directory-id (:id (if existing-general-dir
                          existing-general-dir
                          (dir-model/create dir-data)))}))


(defn create-orgs-dirs-for-users [users]
  (reduce
   (fn [acc user-id]
     (conj
      acc
      {user-id
       (create-general-directory-under-private-org-for-user user-id)}))
   {}
   users))


(defn -main
  [& args]
  (cond
    (= (first args) "dev")  (do (prn "dev dev") (config/read :dev))
    (= (first args) "test") (config/read :test)
    :else                   (config/read :prod))
  (let [notes                 (get-notes-wo-directories)
        unique-users-of-notes (set (map :created-by-id notes))
        users-with-dirs       (create-orgs-dirs-for-users unique-users-of-notes)]
    (for [item notes]
      (note-db/update (users-with-dirs (:created-by-id item)) {:id (:id item)}))))
