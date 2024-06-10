(ns aoc-2018.day-7)

(def re #"Step (\w) must be finished before step (\w) can begin\.")

(defn parse-line
  "Parse a line that describes a step dependency.
   Returns a vector of two elements: 
    - the step that must be finished before the other step, 
    - and the step that can begin after the first step is finished.
  "
  [line]
  (let [[_ before after] (re-find re line)]
    [before after]))

(comment
  (parse-line "Step C must be finished before step A can begin."))
