(ns migrate-notes-without-directories
  (:require [nnts2.db.note :as note-db]))

                                        ;All users will have a "Home" org. with a general channel.
                                        ;all notes without directories would be moved to the above general channel

(defn get-notes-wo-directories []
  (note-db/get {:directory nil}))


(get-notes-wo-directories)
