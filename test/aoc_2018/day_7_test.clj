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

(deftest test-next-step
  (testing "1 input"
    (is (= "A" (sut/next-step [["C" "A"]]))))
  (testing "2 >= inputs"
    (is (= "A" (sut/next-step [["C" "A"] ["C" "F"]])))
    (is (= "A" (sut/next-step [["C" "F"] ["C" "A"] ["C" "B"]])))))

(deftest test-no-previous-steps?
  (is (sut/no-previous-steps? "C" #{["C" "A"] ["C" "F"]}))
  (is (not (sut/no-previous-steps? "A" #{["C" "A"] ["C" "F"]}))))

(deftest test-graph-head
  (is (= "C" (sut/graph-head #{["C" "A"] ["C" "F"]})))
  (is (= "C" (sut/graph-head #{["C" "F"] ["C" "A"] ["F" "E"]})))
  (is (= "C" (sut/graph-head #{["C" "A"] ["C" "F"] ["A" "B"] ["A" "D"] ["B" "E"] ["D" "E"] ["F" "E"]}))))

(deftest test-step-set
  (is (= #{"A" "C"}
         (sut/step-set #{["C" "A"]})))
  (is (= #{"A" "C" "F"}
         (sut/step-set #{["C" "A"] ["C" "F"]})))
  (is (= #{"A" "B" "C" "D" "E" "F"}
         (sut/step-set #{["C" "A"] ["C" "F"] ["A" "B"] ["A" "D"] ["B" "E"] ["D" "E"]})))
  (is (= #{"A" "B" "C" "D" "E" "F"}
         (sut/step-set #{["C" "A"] ["C" "F"] ["A" "B"] ["A" "D"] ["B" "E"] ["D" "E"] ["F" "E"]}))))

