(ns aoc-2018.day-2-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-2 :as sut]))

(deftest test-parse-box-ids
  (is (= (sut/parse-box-ids "abcdef\nbababc\nabbcde\nabcccd\naabcdd\nabcdee\nababab")
         ["abcdef" "bababc" "abbcde" "abcccd" "aabcdd" "abcdee" "ababab"])))

