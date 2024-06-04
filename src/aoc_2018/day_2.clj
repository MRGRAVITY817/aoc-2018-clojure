(ns aoc-2018.day-2
  (:require [clojure.string :as str]))

(defn parse-box-ids
  "Parse box IDs from a string.
   Input is a string with box IDs separated by newlines."
  [input]
  (->> input
       str/split-lines))
