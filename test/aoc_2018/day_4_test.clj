(ns aoc-2018.day-4-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-4 :as sut]))

(deftest test-parse-record
  (testing "has guard id"
    (is (= {:year 1518, :month 11, :day 1, :hour 0, :minute 0, :guard 10, :action "begins shift"}
           (sut/parse-record "[1518-11-01 00:00] Guard #10 begins shift")))
    (is (= {:year 1518, :month 11, :day 1, :hour 0, :minute 0, :guard 99, :action "begins shift"}
           (sut/parse-record "[1518-11-01 00:00] Guard #99 begins shift"))))
  (testing "does not have guard id"
    (is (= {:year 1518, :month 11, :day 1, :hour 0, :minute 5, :guard nil, :action "falls asleep"}
           (sut/parse-record "[1518-11-01 00:05] falls asleep")))
    (is (= {:year 1518, :month 11, :day 1, :hour 0, :minute 5, :guard nil, :action "falls asleep"}
           (sut/parse-record "[1518-11-01 00:05] falls asleep")))))
