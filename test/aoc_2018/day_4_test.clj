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

(deftest test-parse-records
  (testing "before shift"
    (is (= [{:year 2024, :month 06, :day 07, :hour 0, :minute 0, :guard 10, :action "begins shift"}
            {:year 2024, :month 06, :day 07, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
            {:year 2024, :month 06, :day 07, :hour 0, :minute 25, :guard 10, :action "wakes up"}
            {:year 2024, :month 06, :day 07, :hour 0, :minute 30, :guard 10, :action "falls asleep"}
            {:year 2024, :month 06, :day 07, :hour 0, :minute 55, :guard 10, :action "wakes up"}]
           (sut/parse-records "[2024-06-07 00:00] Guard #10 begins shift\n[2024-06-07 00:05] falls asleep\n[2024-06-07 00:25] wakes up\n[2024-06-07 00:30] falls asleep\n[2024-06-07 00:55] wakes up"))))
  (testing "has a shift"
    (is (= [{:year 2024, :month 6, :day 7, :hour 0, :minute 0, :guard 10, :action "begins shift"}
            {:year 2024, :month 6, :day 7, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
            {:year 2024, :month 6, :day 7, :hour 0, :minute 25, :guard 10, :action "wakes up"}
            ;; Shift to guard 99
            {:year 2024, :month 6, :day 8, :hour 0, :minute 0, :guard 99, :action "begins shift"}
            {:year 2024, :month 6, :day 8, :hour 0, :minute 30, :guard 99, :action "falls asleep"}
            {:year 2024, :month 6, :day 8, :hour 0, :minute 55, :guard 99, :action "wakes up"}]
           (sut/parse-records "[2024-06-07 00:00] Guard #10 begins shift\n[2024-06-07 00:05] falls asleep\n[2024-06-07 00:25] wakes up\n[2024-06-08 00:00] Guard #99 begins shift\n[2024-06-08 00:30] falls asleep\n[2024-06-08 00:55] wakes up")))))

(deftest test-sort-chronologically
  (is (= [{:year 2024, :month 6, :day 7, :hour 0, :minute 0, :guard 10, :action "begins shift"}
          {:year 2024, :month 6, :day 7, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
          {:year 2024, :month 6, :day 7, :hour 0, :minute 25, :guard 10, :action "wakes up"}
          {:year 2024, :month 6, :day 8, :hour 0, :minute 0, :guard 99, :action "begins shift"}
          {:year 2024, :month 6, :day 8, :hour 0, :minute 30, :guard 99, :action "falls asleep"}
          {:year 2024, :month 6, :day 8, :hour 0, :minute 55, :guard 99, :action "wakes up"}]
         (sut/sort-chronologically [{:year 2024, :month 6, :day 7, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                                    {:year 2024, :month 6, :day 8, :hour 0, :minute 0, :guard 99, :action "begins shift"}
                                    {:year 2024, :month 6, :day 7, :hour 0, :minute 0, :guard 10, :action "begins shift"}
                                    {:year 2024, :month 6, :day 8, :hour 0, :minute 30, :guard 99, :action "falls asleep"}
                                    {:year 2024, :month 6, :day 7, :hour 0, :minute 25, :guard 10, :action "wakes up"}
                                    {:year 2024, :month 6, :day 8, :hour 0, :minute 55, :guard 99, :action "wakes up"}]))))
(deftest test-partition-linked
  (is (= '[(0 1) (1 2) (2 3) (3 4) (4 5) (5 6) (6 7) (7 8) (8 9)]
         (sut/partition-linked (range 10)))))

(deftest test-records->sleep-record
  (testing "sleep record"
    (is (= {5 2, 6 2, 7 1, 8 1}
           (sut/records->sleep-record [{:year 2024, :month 6, :day 7, :hour 0, :minute 0, :guard 10, :action "begins shift"}
                                       {:year 2024, :month 6, :day 7, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                                       {:year 2024, :month 6, :day 7, :hour 0, :minute 7, :guard 10, :action "wakes up"}
                                       {:year 2024, :month 6, :day 8, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                                       {:year 2024, :month 6, :day 8, :hour 0, :minute 9, :guard 10, :action "wakes up"}])))))

(deftest test-laziest-guard
  (testing "laziest guard"
    (is (= [10 {7 2, 20 1, 24 1, 39 1, 46 1, 54 1, 15 1, 48 1, 50 1, 21 1, 31 2, 32 2, 40 1, 33 2, 13 2, 22 1,
                36 1, 41 1, 43 1, 44 1, 6 2, 51 1, 34 2, 17 1, 12 2, 23 1, 47 1, 35 1, 19 1, 11 2, 9 2, 5 2,
                14 2, 45 1, 53 1, 16 1, 38 1, 30 2, 10 2, 18 1, 52 1, 42 1, 37 1, 8 2, 49 1}]
           (sut/laziest-guard [;; Guard 10: Slept total 60 mins
                               {:year 2024, :month 6, :day 7, :hour 0, :minute 0, :guard 10, :action "begins shift"}
                               {:year 2024, :month 6, :day 7, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                               {:year 2024, :month 6, :day 7, :hour 0, :minute 25, :guard 10, :action "wakes up"}
                               {:year 2024, :month 6, :day 7, :hour 0, :minute 30, :guard 10, :action "falls asleep"}
                               {:year 2024, :month 6, :day 7, :hour 0, :minute 55, :guard 10, :action "wakes up"}
                               {:year 2024, :month 6, :day 8, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                               {:year 2024, :month 6, :day 8, :hour 0, :minute 15, :guard 10, :action "wakes up"}
                               {:year 2024, :month 6, :day 8, :hour 0, :minute 30, :guard 10, :action "falls asleep"}
                               {:year 2024, :month 6, :day 8, :hour 0, :minute 35, :guard 10, :action "wakes up"}
                                      ;; Guard 99: Slept total 25 mins
                               {:year 2024, :month 6, :day 8, :hour 0, :minute 0, :guard 99, :action "begins shift"}
                               {:year 2024, :month 6, :day 8, :hour 0, :minute 30, :guard 99, :action "falls asleep"}
                               {:year 2024, :month 6, :day 8, :hour 0, :minute 55, :guard 99, :action "wakes up"}
                                      ;; Guard 20: Slept total 35 mins
                               {:year 2024, :month 6, :day 9, :hour 0, :minute 0, :guard 20, :action "begins shift"}
                               {:year 2024, :month 6, :day 9, :hour 0, :minute 5, :guard 20, :action "falls asleep"}
                               {:year 2024, :month 6, :day 9, :hour 0, :minute 25, :guard 20, :action "wakes up"}
                               {:year 2024, :month 6, :day 9, :hour 0, :minute 30, :guard 20, :action "falls asleep"}
                               {:year 2024, :month 6, :day 9, :hour 0, :minute 45, :guard 20, :action "wakes up"}])))))
