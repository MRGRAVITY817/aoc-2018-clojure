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

(defn parse-records
  "Parse records from given text input.
   If there's a record that doesn't contain the guard id, 
   this function will try to find the guard id from the previous record.

   Example:
   ```
   (parse-records \"2024-06-07 00:00 Guard #10 begins shift\n
                   2024-06-07 00:05 falls asleep\")
   ;; [{:year 2024, :month 06, :day 07, :hour 0, :minute 0, :guard 10, :action \"begins shift\"}
   ;;  {:year 2024, :month 06, :day 07, :hour 0, :minute 5, :guard 10, :action \"falls asleep\"}]
   ```
  "
  [input]
  (let [lines (str/split-lines input)]
    (loop [lines   lines
           records []
           guard   nil]
      (if (empty? lines)
        records
        (let [record (parse-record (first lines))
              guard  (or (:guard record) guard)]
          (recur (rest lines)
                 (conj records (assoc record :guard guard))
                 guard))))))

(comment
  (parse-record "[1518-11-01 00:00] Guard #10 begins shift")
  (parse-record "[1518-11-01 00:05] falls asleep") ;
  (parse-records "[2024-06-07 00:00] Guard #10 begins shift\n[2024-06-07 00:05] falls asleep"))

