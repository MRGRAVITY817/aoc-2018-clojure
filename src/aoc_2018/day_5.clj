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

#_(defn react
    "Remove all adjacent pairs of letters that are the same letter but different case.

  Examples:
  ```
  (react \"aA\") => \"\"
  (react \"dabAcCaCBAcCcaDA\") => \"dabCBAcaDA\"
  ```
  "
    [input]
    (if (reactable? (input))))

(comment
  (first "hello"))

