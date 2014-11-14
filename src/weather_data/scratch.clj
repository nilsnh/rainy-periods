;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.

(def init [[["test"]][["hello"]]])

(defn nested-array [x] (last (last x)))

(def x (conj (butlast init) [(conj (nested-array init) ["world"])]))

(conj (conj (nested-array init) ["world"]) (butlast init))

(conj x [["another world"]])

[[[1] [2]]
 [[1] [3] [4]]]
