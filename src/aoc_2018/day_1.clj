(ns aoc-2018.day-1
  (:require [clojure.string :as str]))

(defn parse-frequency-changes
  "Parse frequency changes from a string.
   Input is a string with frequency changes separated by newlines."
  [input]
  (->> input
       (str/split-lines)
       (map #(Integer/parseInt %))))

(defn resulting-frequency
  "Calculate the resulting frequency from a list of frequency changes."
  [input]
  (->> input
       parse-frequency-changes
       (reduce + 0)))

(defn day-1 []
  (->> (slurp "resources/day_1_input.txt")
       (resulting-frequency)))

(comment
  (day-1)) ; 599
