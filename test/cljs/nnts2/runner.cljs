(ns nnts2.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [nnts2.core-test]))

(doo-tests 'nnts2.core-test)
