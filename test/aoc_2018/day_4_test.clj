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

#_(deftest test-laziest-guard
    (testing "laziest guard"
      (is (= 240
             (sut/laziest-guard [{:year 2024, :month 6, :day 7, :hour 0, :minute 0, :guard 10, :action "begins shift"}
                                 {:year 2024, :month 6, :day 7, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                                 {:year 2024, :month 6, :day 7, :hour 0, :minute 25, :guard 10, :action "wakes up"}
                                 {:year 2024, :month 6, :day 7, :hour 0, :minute 30, :guard 10, :action "falls asleep"}
                                 {:year 2024, :month 6, :day 7, :hour 0, :minute 55, :guard 10, :action "wakes up"}
                               ;; Guard 10: Slept total 45 mins
                                 {:year 2024, :month 6, :day 8, :hour 0, :minute 0, :guard 99, :action "begins shift"}
                                 {:year 2024, :month 6, :day 8, :hour 0, :minute 30, :guard 99, :action "falls asleep"}
                                 {:year 2024, :month 6, :day 8, :hour 0, :minute 55, :guard 99, :action "wakes up"}
                               ;; Guard 99: Slept total 25 mins
                                 ])))))
