(ns aoc-2018.day-5
  (:require [clojure.string :as str]))

(defn reactable?
  "Return true if the two characters are the same letter but different case.

  Examples:
  ```
  (reactable? \\a \\A) => true
  (reactable? \\B \\b) => true
  (reactable? \\a \\a) => false
  (reactable? \\B \\a) => false
  ```
  "
  [a b]
  (and (not= a b)
       (= (str/lower-case a) (str/lower-case b))))

(defn react
  "Remove all adjacent pairs of letters that are 'reactable'.
   Once removed, it returns a vector of letters haven't reacted.

  Examples:
  ```
  (react \"aA\") => []
  (react \"dabAcCaCBAcCcaDA\") => [\\d \\a \\b \\C \\B \\A \\c \\a \\D \\A]
  ```
  "
  ([chars] (react [] chars))
  ([acc [c1 c2 & chars]]
   (cond (not c1)            acc
         (not c2)            (conj acc c1)
         (reactable? c1 c2)  (if (seq acc)
                               (recur (pop acc) (cons (peek acc) chars))
                               (recur acc chars))
         :else               (recur (conj acc c1) (cons c2 chars)))))

(defn- parse-input
  "Read the input file and return the content as a string."
  [filename]
  (-> filename slurp str/trim))

(defn day-5-part-1
  "Return the length of the polymer after fully reacting."
  [filename]
  (-> filename parse-input ;; parsing
      react count))        ;; main logic

(comment
  (day-5-part-1 "resources/day_5_input.txt") ; 10888

  ;; helper functions
  (react "aA") ; []
  (react "abBA") ; []
  (react "abAB") ; [\a \b \A \B]
  (react "dabAcCaCBAcCcaDA") ; [\d \a \b \C \B \A \c \a \D \A]
  )


