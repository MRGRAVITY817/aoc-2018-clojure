(ns aoc-2018.day-1-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-1 :as sut]))

(deftest test-resulting-frequency
  (testing "+1 +1 +1"
    (is (= 3 (sut/resulting-frequency "+1\r\n+1\r\n+1"))))
  (testing "-1 +1 -1"
    (is (= -1 (sut/resulting-frequency "-1\r\n+1\r\n-1"))))
  (testing "-2 -3 +4 -15 -15 +18 -7 +11 -16 -14 +2 -6 +16 +6"
    (is (= -21 (sut/resulting-frequency "-2\r\n-3\r\n+4\r\n-15\r\n-15\r\n+18\r\n-7\r\n+11\r\n-16\r\n-14\r\n+2\r\n-6\r\n+16\r\n+6")))))

(deftest test-parse-frequency-changes
  (testing "+1 +1 +1"
    (is (= [1 1 1] (sut/parse-frequency-changes "+1\r\n+1\r\n+1"))))
  (testing "-1 +1 -1"
    (is (= [-1 1 -1] (sut/parse-frequency-changes "-1\r\n+1\r\n-1"))))
  (testing "-2 -3 +4 -15 -15 +18 -7 +11 -16 -14 +2 -6 +16 +6"
    (is (= [-2 -3 4 -15 -15 18 -7 11 -16 -14 2 -6 16 6] (sut/parse-frequency-changes "-2\r\n-3\r\n+4\r\n-15\r\n-15\r\n+18\r\n-7\r\n+11\r\n-16\r\n-14\r\n+2\r\n-6\r\n+16\r\n+6")))))
