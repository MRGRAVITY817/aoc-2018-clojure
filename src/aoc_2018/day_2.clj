(ns aoc-2018.day-2
  (:require [clojure.string :as str]
            [clojure.set :refer [map-invert]]))

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
  (let [dict (->> input frequencies map-invert)]
    (contains? dict n)))

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

(defn similar-box-ids
  "Find the two box IDs that differ by exactly one character."
  [box-ids]
  (let [pairs (for [id1 box-ids
                    id2 box-ids
                    :when (= (char-diff id1 id2) 1)]
                [id1 id2])]
    (first pairs)))

(defn overlapping-letters
  "Find the overlapping letters of the two words."
  [[word1 word2]]
  (->> (map vector word1 word2)
       (filter (fn [[c1 c2]] (= c1 c2)))
       (map first)
       (apply str)))

(defn day-2-part-2
  "Find the overlapping letters of the two box IDs that differ by exactly one character."
  [filename]
  (-> filename
      slurp
      parse-box-ids
      similar-box-ids
      overlapping-letters))

(comment
  (contains-exactly-n-of-any-letter? "abcdef" 2) ; false
  (contains-exactly-n-of-any-letter? "abadef" 2) ; true
  (contains-exactly-n-of-any-letter? "abcdef" 3) ; false
  (contains-exactly-n-of-any-letter? "abadea" 3) ; true
  (checksum ["abcdef" "bababc" "abbcde" "abcccd" "aabcdd" "abcdee" "ababab"]) ; 12
  (day-2-part-1 "resources/day_2_input.txt") ; 5166
  (overlapping-letters ["aaaab" "aaabb"])  ; "aaab"
  (day-2-part-2 "resources/day_2_input.txt") ; "cypueihajytordkgzxfqplbwn"
  )




