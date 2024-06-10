(ns aoc-2018.day-7-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-7 :as sut]))

(deftest test-parse-line
  (is (= ["C" "A"] (sut/parse-line "Step C must be finished before step A can begin.")))
  (is (= ["C" "F"] (sut/parse-line "Step C must be finished before step F can begin.")))
  (is (= ["A" "B"] (sut/parse-line "Step A must be finished before step B can begin."))))

