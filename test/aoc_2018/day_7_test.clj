(ns aoc-2018.day-7-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-7 :as sut]))

(deftest test-parse-line
  (is (= ["C" "A"] (sut/parse-line "Step C must be finished before step A can begin.")))
  (is (= ["C" "F"] (sut/parse-line "Step C must be finished before step F can begin.")))
  (is (= ["A" "B"] (sut/parse-line "Step A must be finished before step B can begin."))))

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


