(ns apqc.lexicon-contract-test
  (:require [clojure.edn :as edn]
            [clojure.test :refer [deftest is]]))

(def lexicon-files
  ["coverageSnapshot" "emitEvent" "getProcess"
   "materializeSubprocessDid" "materializeSubprocesses" "processCategory"])

(deftest canonical-lexicons
  (doseq [name lexicon-files]
    (let [document (edn/read-string (slurp (str "lex/" name ".edn")))]
      (is (= 1 (get document "lexicon")) name)
      (is (= (str "com.etzhayyim.apqc." name) (get document "id")) name)
      (is (map? (get document "defs")) name))))
