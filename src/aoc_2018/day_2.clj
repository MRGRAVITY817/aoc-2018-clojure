(ns aoc-2018.day-2
  (:require [clojure.string :as str]))

;; Part 1

(defn parse-box-ids
  "Parse box IDs from a string.
   Input is a string with box IDs separated by newlines."
  [input]
  (-> input
      str/split-lines))

(defn contains-exactly-n-of-any-letter?
  "Check if string contains exactly n of any letter.

   Example:

   ```
   (contains-exactly-n-of-any-letter? \"abcdef\" 2) ; false
   (contains-exactly-n-of-any-letter? \"abadef\" 2) ; true
   ```
  "
  [input n]
  (->> input
       frequencies
       vals
       (some #(= n %))
       boolean))

(defn checksum
  "Calculate the checksum of a list of box IDs."
  [box-ids]
  (* (count (filter #(contains-exactly-n-of-any-letter? % 2) box-ids))
     (count (filter #(contains-exactly-n-of-any-letter? % 3) box-ids))))

(defn day-2-part-1
  "Calculate the checksum of a file."
  [filename]
  (-> filename
      slurp
      parse-box-ids
      checksum))

;; Part 2

(defn char-diff
  "Calculate the number of different (by location) characters between two strings."
  [s1 s2]
  (->> (map vector s1 s2)
       (filter (fn [[c1 c2]] (not= c1 c2)))
       count))

(comment
  (contains-exactly-n-of-any-letter? "abcdef" 2) ; false
  (contains-exactly-n-of-any-letter? "abadef" 2) ; true
  (contains-exactly-n-of-any-letter? "abcdef" 3) ; false
  (contains-exactly-n-of-any-letter? "abadea" 3) ; true
  (checksum ["abcdef" "bababc" "abbcde" "abcccd" "aabcdd" "abcdee" "ababab"]) ; 12
  (day-2-part-1 "resources/day_2_input.txt") ; 5166
;
  )
