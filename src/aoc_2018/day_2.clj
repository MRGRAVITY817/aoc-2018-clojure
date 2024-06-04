(ns aoc-2018.day-2
  (:require [clojure.string :as str]))

(defn parse-box-ids
  "Parse box IDs from a string.
   Input is a string with box IDs separated by newlines."
  [input]
  (->> input
       str/split-lines))

(defn contains-exactly-n-of-any-letter?
  "Check if string contains exactly n of any letter.

   Example:

   ```
   (contains-exactly-n-of-any-letter? \"abcdef\" 2) ; false
   (contains-exactly-n-of-any-letter? \"abadef\" 2) ; true
   ```
  "
  [box-id n]
  (->> box-id
       frequencies
       vals
       (some #(= n %))
       boolean))

(comment
  (contains-exactly-n-of-any-letter? "abcdef" 2) ; false
  (contains-exactly-n-of-any-letter? "abadef" 2) ; true
  (contains-exactly-n-of-any-letter? "abcdef" 3) ; false
  (contains-exactly-n-of-any-letter? "abadea" 3) ; true
;
  )
