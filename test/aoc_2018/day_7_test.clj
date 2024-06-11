(ns aoc-2018.day-7-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-7 :as sut]))

(deftest test-parse-line
  (is (= {:current "C" :next "A"}
         (sut/parse-line "Step C must be finished before step A can begin.")))
  (is (= {:current "C" :next "F"}
         (sut/parse-line "Step C must be finished before step F can begin.")))
  (is (= {:current "A" :next "B"}
         (sut/parse-line "Step A must be finished before step B can begin."))))

(deftest test-lines->deps-and-steps
  (is (= {:deps {"A" #{"C"}, "F" #{"C"}, "B" #{"A"}, "D" #{"A"}, "E" #{"F" "B" "D"}},
          :steps #{"C" "F" "B" "A" "D" "E"}}
         (sut/lines->deps-and-steps [{:current "C" :next "A"}
                                     {:current "C" :next "F"}
                                     {:current "A" :next "B"}
                                     {:current "A" :next "D"}
                                     {:current "B" :next "E"}
                                     {:current "D" :next "E"}
                                     {:current "F" :next "E"}])))
  ;; Same as above, but with different order
  (is (= {:deps {"B" #{"A"}, "D" #{"A"}, "E" #{"F" "B" "D"}, "A" #{"C"}, "F" #{"C"}},
          :steps #{"C" "F" "B" "A" "D" "E"}}
         (sut/lines->deps-and-steps [{:current "A" :next "B"}
                                     {:current "A" :next "D"}
                                     {:current "B" :next "E"}
                                     {:current "D" :next "E"}
                                     {:current "F" :next "E"}
                                     {:current "C" :next "A"}
                                     {:current "C" :next "F"}]))))

(deftest test-order-steps
  (testing "1 connection"
    (is (= ["B" "A"]
           (sut/order-steps {"A" #{"B"}}  #{"A" "B"}))))
  (testing "more connections"
    (is (= ["C" "A" "B" "D" "F" "E"]
           (sut/order-steps {"A" #{"C"}, "F" #{"C"}, "B" #{"A"}, "D" #{"A"}, "E" #{"F" "B" "D"}}
                            #{"C" "F" "B" "A" "D" "E"})))))

;; how to solve part 2?
;; - manage a vector that has 5 slots (for each worker), containing remaining seconds
;; - each seconds will be decremented for every recursive call, while we accumulate the total seconds at the same time
;; - if a worker has 0 seconds left, it will be available for the next task (idle)
;; - idle workers will be assigned to the next task (lexicographically first & doesn't have any preceding steps)
;; - if all workers are idle, it means we have finished all tasks

;; loop states
;; - deps and steps
;; - remaining seconds for each worker (5 slots)
;; - steps which are being worked on by each worker
;; - total seconds
;; - done steps
