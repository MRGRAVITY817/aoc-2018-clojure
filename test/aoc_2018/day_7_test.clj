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

(deftest test-after-a-second
  (is (= {:remaining [0 0 1 2 3]
          :status [nil nil "C" "D" "E"]
          :done ["A" "B"]
          :steps #{"C" "D" "E"}}
         (sut/after-a-second [0 1 2 3 4]
                             [nil "B" "C" "D" "E"]
                             ["A"]
                             #{"B" "C" "D" "E"}))))

(deftest test-get-finished-step?
  (is (= "B"
         (sut/get-finished-step? [0 1 2 3 4] [nil "B" "C" "D" "E"]))))

(deftest test-idle-worker
  (is (= 0
         (sut/idle-worker [0 1 2 3 4])))
  (is (= 3
         (sut/idle-worker [1 1 2 0 0]))))
