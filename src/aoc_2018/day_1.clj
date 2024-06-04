(ns aoc-2018.day-1
  (:require [clojure.string :as str]))

(defn parse-frequency-changes
  "Parse frequency changes from a string.
   Input is a string with frequency changes separated by newlines."
  [input]
  (->> input
       str/split-lines
       (map #(Integer/parseInt %))))

(defn resulting-frequency
  "Calculate the resulting frequency from a list of frequency changes."
  [input]
  (->> input
       (reduce + 0)))

(defn day-1-part-1
  "Calculate the resulting frequency from a file."
  [input]
  (->> input
       slurp
       parse-frequency-changes
       resulting-frequency))

(defn twice-reached-frequency
  "Find the first frequency that is reached twice."
  [input]
  (loop [frequencies       #{0}
         current-frequency 0
         frequency-changes (cycle input)]
    (let [new-frequency (+ current-frequency (first frequency-changes))]
      (if (contains? frequencies new-frequency)
        new-frequency
        (recur (conj frequencies new-frequency)
               new-frequency
               (rest frequency-changes))))))

(defn day-1-part-2
  "Find the first frequency that is reached twice from a file."
  [input]
  (-> input
      slurp
      parse-frequency-changes
      twice-reached-frequency))

(comment
  (day-1-part-1 "resources/day_1_input.txt") ; 599
  (day-1-part-2 "resources/day_1_input.txt")) ; 81204
