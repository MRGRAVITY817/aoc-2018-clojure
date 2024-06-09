(ns aoc-2018.day-5
  (:require [clojure.string :as string]))

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
       (= (string/lower-case a) (string/lower-case b))))

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
  (-> filename slurp string/trim))

(defn day-5-part-1
  "Return the length of the polymer after fully reacting."
  [filename]
  (-> filename parse-input ;; parsing
      react count))        ;; main logic

(defn remove-alphabet
  "Remove all instances of the given alphabet from the input string.
   It will remove both lower and upper case of the given alphabet.

  Examples:
  ```
  (remove-alphabet \"abBA\" \\a) => \"bB\"
  (remove-alphabet \"dabAcCaCBAcCcaDA\" \\a) => \"dbcCCBcCcD\"
  ```
  "
  [input alphabet]
  (-> input
      (string/replace (string/lower-case alphabet) "")
      (string/replace (string/upper-case alphabet) "")))

(defn day-5-part-2
  "Return the minimum length of the polymer after removing the alphabet and reacting fully."
  [filename]
  (let [parsed-input    (parse-input filename)
        reacted-lengths (map #(-> parsed-input (remove-alphabet %) react count)
                             "abcdefghijklmnopqrstuvwxyz")
        min-length      (apply min reacted-lengths)]
    min-length))

(comment
  (day-5-part-1 "resources/day_5_input.txt") ; 10888
  (day-5-part-2 "resources/day_5_input.txt") ; 6952

  ;; helper functions
  (react "aA") ; []
  (react "abBA") ; []
  (react "abAB") ; [\a \b \A \B]
  (react "dabAcCaCBAcCcaDA") ; [\d \a \b \C \B \A \c \a \D \A]
  )




