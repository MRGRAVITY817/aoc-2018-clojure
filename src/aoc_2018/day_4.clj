(ns aoc-2018.day-4
  (:require [clojure.string :as str]
            [aoc-2018.utils :as utils]
            [malli.core :as m]
            [malli.dev :as mdev]))

(mdev/start!)

(def Record
  "Record is a map that contains the timestamp, guard id, and action."
  [:map
   [:year :int]
   [:month :int]
   [:day :int]
   [:hour :int]
   [:minute :int]
   [:guard [:maybe :int]]
   [:action :string]])

(def SleepRecord
  "SleepRecord is a vector of {guard-id sleeping-minute} map."
  [:vector
   [:map
    [:guard :int]
    [:minute :int]]])

(def re #"\[(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2})\] (Guard #\d+ )?(.*)")

(m/=> parse-record [:=> [:cat :string] Record])
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

(m/=> parse-records [:=> [:cat :string] [:sequential Record]])
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
  (let [lines (-> input str/split-lines sort)]
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

(m/=> records->sleep-record [:=> [:cat [:sequential Record]] SleepRecord])
(defn records->sleep-record
  "Convert records into a sleep record.
   The sleep record is a seq of {guard-id sleeping-minute} map.

   For example, if the guard #10 slept at minute 5 and 6, the sleep record would be:
   ```
   [{:guard 10, :minute 5} 
    {:guard 10, :minute 6}]
   ```
  "
  [records]
  (->> records
       utils/partition-linked
       (filter (fn [[start end]]
                 (and (= (:action start) "falls asleep")
                      (= (:action end) "wakes up"))))
       (mapcat (fn [[start end]]
                 (->> (range (:minute start) (:minute end))
                      (map (fn [minute] {:guard (:guard start), :minute minute})))))))

(m/=> laziest-guard [:=> [:cat [:sequential Record]] SleepRecord])
(defn laziest-guard
  "Return the sleep record of the guard that sleeps the most from given list of records."
  [records]
  (let [guards        (->> records
                           (group-by :guard)
                           (mapcat (fn [[_ records]] (records->sleep-record records))))
        laziest-guard (->> guards
                           (map :guard)
                           utils/most-frequent)]
    (filter #(= (:guard %) laziest-guard) guards)))

(m/=> most-asleep-min-x-id [:=> [:cat SleepRecord] :int])
(defn most-asleep-min-x-id
  "Return the multiplied value between 
   - guard-id
   - the minute the guard was most asleep 
   ... from given sleep record vector."
  [sleep-record]
  (let [{:keys [guard minute]} (utils/most-frequent sleep-record)]
    (* guard minute)))

(defn day-4-part-1
  "Calculate the ID of the laziest guard multiplied by the minute the guard was most asleep."
  [filename]
  (-> filename slurp parse-records         ;; parsing
      laziest-guard most-asleep-min-x-id)) ;; main logic

(defn day-4-part-2
  "Calculate the ID of the guard that is most frequently asleep on the same minute, 
   multiplied by the minute."
  [filename]
  (->> filename slurp parse-records                 ;; parsing
       records->sleep-record most-asleep-min-x-id)) ;; main logic      

(comment
  (day-4-part-1 "resources/day_4_input.txt") ; 39584
  (day-4-part-2 "resources/day_4_input.txt")
  ;; helper functions
  (parse-record "[1518-11-01 00:00] Guard #10 begins shift")
  (parse-record "[1518-11-01 00:05] falls asleep") ;
  (parse-records "[2024-06-07 00:00] Guard #10 begins shift\n[2024-06-07 00:05] falls asleep")
  (first (group-by :guard (parse-records "[2024-06-07 00:00] Guard #10 begins shift\n[2024-06-07 00:05] falls asleep\n[2024-06-07 00:25] wakes up\n[2024-06-07 00:30] falls asleep\n[2024-06-07 00:55] wakes up\n[2024-06-08 00:00] Guard #99 begins shift\n[2024-06-08 00:30] falls asleep\n[2024-06-08 00:55] wakes up")))

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



