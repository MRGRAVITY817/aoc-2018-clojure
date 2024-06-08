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

(defn sort-chronologically
  "Sort records chronologically."
  [records]
  (sort-by (fn [record]
             (let [{:keys [year month day hour minute]} record]
               [year month day hour minute]))
           records))

(defn partition-linked
  "Partition records into linked pairs.

   For example:
   ```
   [1 2 3 4 ...] => [(1 2) (2 3) (3 4) ...]
   ```
   "
  [records]
  (loop [records records
         partitions []]
    (if (= (count records) 1)
      partitions
      (recur (rest records)
             (conj partitions (take 2 records))))))

(defn records->sleep-record
  "Convert records into a sleep record.
   The sleep record is a map, which key is the minute the guard slept and the value is the frequency.

   For example, if the guard slept at minute 5 and 6, the sleep record would be:
   ```
   {5 1, 6 1}
   ```
  "
  [records]
  (->> records
       partition-linked
       (filter (fn [[start end]]
                 (and (= (:action start) "falls asleep")
                      (= (:action end) "wakes up"))))
       (map (fn [[start end]]
              (range (:minute start) (:minute end))))
       (apply concat)
       frequencies))

(defn laziest-guard
  "Return the sleep record of the guard that sleeps the most from given list of records."
  [records]
  (let [sorted-records   (sort-chronologically records)
        guards           (->> sorted-records
                              (group-by :guard)
                              (map (fn [[guard records]] [guard (records->sleep-record records)]))
                              (sort-by (fn [[_ sleep-record]] (apply + (vals sleep-record)))))]

    (last guards)))

(defn most-asleep-min-x-id
  "Return the multiplied value between 
   - guard-id
   - the minute the guard was most asleep 
   ... from given [guard-id sleep-record] vector."
  [[guard-id sleep-record]]
  (let [minute (->> sleep-record
                    (sort-by val)
                    last
                    key)]
    (* guard-id minute)))

(comment
  (parse-record "[1518-11-01 00:00] Guard #10 begins shift")
  (parse-record "[1518-11-01 00:05] falls asleep") ;
  (parse-records "[2024-06-07 00:00] Guard #10 begins shift\n[2024-06-07 00:05] falls asleep")
  (first (group-by :guard (parse-records "[2024-06-07 00:00] Guard #10 begins shift\n[2024-06-07 00:05] falls asleep\n[2024-06-07 00:25] wakes up\n[2024-06-07 00:30] falls asleep\n[2024-06-07 00:55] wakes up\n[2024-06-08 00:00] Guard #99 begins shift\n[2024-06-08 00:30] falls asleep\n[2024-06-08 00:55] wakes up")))

  (partition-linked (range 10)) ; [(0 1) (1 2) (2 3) (3 4) (4 5) (5 6) (6 7) (7 8) (8 9)]
  (records->sleep-record [10 [{:year 2024, :month 6, :day 7, :hour 0, :minute 0, :guard 10, :action "begins shift"}
                              {:year 2024, :month 6, :day 7, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                              {:year 2024, :month 6, :day 7, :hour 0, :minute 25, :guard 10, :action "wakes up"}
                              {:year 2024, :month 6, :day 7, :hour 0, :minute 30, :guard 10, :action "falls asleep"}
                              {:year 2024, :month 6, :day 7, :hour 0, :minute 55, :guard 10, :action "wakes up"}
                              {:year 2024, :month 6, :day 8, :hour 0, :minute 5, :guard 10, :action "falls asleep"}
                              {:year 2024, :month 6, :day 8, :hour 0, :minute 20, :guard 10, :action "wakes up"}
                              {:year 2024, :month 6, :day 8, :hour 0, :minute 50, :guard 10, :action "falls asleep"}
                              {:year 2024, :month 6, :day 8, :hour 0, :minute 55, :guard 10, :action "wakes up"}]])
  (most-asleep-min-x-id [10 {5 2, 6 1, 7 1}]) ; 50
  )



