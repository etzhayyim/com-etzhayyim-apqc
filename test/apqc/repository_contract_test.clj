(ns apqc.repository-contract-test
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.test :refer [deftest is]]))

(defn- read-edn [path] (edn/read-string (slurp path)))

(deftest canonical-repository-shape
  (doseq [path ["manifest.edn" "identity.edn" "dependencies.edn"
                "repository-contracts.edn" "kotoba.app.edn"
                "kotoba/apqc-pcf.kotoba.edn"]]
    (is (some? (read-edn path)) path))
  (is (= "apqc" (get (read-edn "manifest.edn") "name")))
  (is (not (.exists (io/file "actor-manifest.jsonld"))))
  (is (not (.exists (io/file "kotoba/run_tests.sh"))))
  (is (not (.exists (io/file "kotoba/apqc-coordinator.wasm"))))
  (is (.exists (io/file "wire/actor-manifest.jsonld")))
  (is (= 6 (count (filter #(.isFile %)
                           (file-seq (io/file "wire/lexicons")))))))

(deftest dependencies-are-immutable-flat-west-references
  (let [deps (:dependencies (read-edn "dependencies.edn"))]
    (is (= #{'etzhayyim/root 'org.kotoba-lang/kotoba}
           (set (map :dependency/id deps))))
    (is (every? #(re-matches #"[0-9a-f]{40}" (:dependency/revision %)) deps))
    (is (= "orgs/kotoba-lang/kotoba"
           (:dependency/west-path (second deps))))))
