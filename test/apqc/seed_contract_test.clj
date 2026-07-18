(ns apqc.seed-contract-test
  (:require [clojure.edn :as edn]
            [clojure.set :as set]
            [clojure.test :refer [deftest is]]))

(def seed-document (edn/read-string (slurp "kotoba/apqc-pcf.kotoba.edn")))
(def rows (:seed seed-document))
(def attributes (set (map :db/ident (:attributes seed-document))))
(def ids (set (map :db/id rows)))

(deftest canonical-pcf-census
  (is (= 713 (count rows)))
  (is (= {1 13, 2 72, 3 352, 4 276}
         (frequencies (map :apqc.process/level rows))))
  (is (= 713 (count (set (map :apqc.process/code rows))))))

(deftest seed-references-and-sourcing-are-valid
  (is (every? #(or (nil? (:apqc.process/parent %))
                   (contains? ids (:apqc.process/parent %)))
              rows))
  (is (every? #(contains? #{:authoritative :representative :synthesized}
                          (:apqc/sourcing %))
              rows))
  (is (set/subset?
       (disj (set (filter namespace (mapcat keys rows))) :db/id)
       attributes)))

(deftest authoritative-public-level-one
  (let [level-one (filter #(= 1 (:apqc.process/level %)) rows)]
    (is (= 13 (count level-one)))
    (is (every? #(= :authoritative (:apqc/sourcing %)) level-one))))
