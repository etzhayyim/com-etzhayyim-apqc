(require '[clojure.test :as t])

(doseq [ns-sym '[apqc.seed-contract-test
                  apqc.lexicon-contract-test
                  apqc.murakumo-test
                  apqc.repository-contract-test]]
  (require ns-sym))

(let [result (apply t/run-tests
                    '[apqc.seed-contract-test
                      apqc.lexicon-contract-test
                      apqc.murakumo-test
                      apqc.repository-contract-test])]
  (System/exit (if (zero? (+ (:fail result) (:error result))) 0 1)))
