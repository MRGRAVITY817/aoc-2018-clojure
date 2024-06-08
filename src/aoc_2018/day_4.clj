(ns aoc-2018.day-4
  (:require [clojure.string :as str]))

(def re #"\[(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2})\] (Guard #\d+ )?(.*)")

(defn parse-record
  "Parse a record into a `Record` map.
   The record basically contains the year, month, day, hour, minute and action.
   It may or may not contain the guard id.

   Example:
   ```
   ;; Record contains the guard id
   (parse-record \"[1518-11-01 00:00] Guard #10 begins shift\")
   ;; => {:year 1518, :month 11, :day 1, :hour 0, :minute 0, :guard 10, :action \"begins shift\"}

   ;; Record does not contain the guard id
   (parse-record \"[1518-11-01 00:05] falls asleep\")
   ;; => {:year 1518, :month 11, :day 1, :hour 0, :minute 5, :guard nil, :action \"falls asleep\"}
   ```
  "
  [line]
  (let [[_ year month day hour minute guard action] (re-find re line)]
    {:year (Integer/parseInt year)
     :month (Integer/parseInt month)
     :day (Integer/parseInt day)
     :hour (Integer/parseInt hour)
     :minute (Integer/parseInt minute)
     :guard (when guard (->> guard
                             (re-find #"\d+")
                             Integer/parseInt))
     :action action}))



