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

(defn next-step
  "Given a list of connections from a specific step to another step, 
   return the next step that can be taken.
    
   For example, when given a list of connections [[\"C\" \"A\"], [\"C\" \"F\"]],
   we take \"A\" as the next step, since it is alphabetically first than F.
   "
  [connections]
  (->> connections
       (sort)
       (first)
       (second)))

(comment
  (parse-line "Step C must be finished before step A can begin.") ; ["C" "A"]
  (next-step [["C" "A"]]) ; "A"
  (next-step [["C" "A"] ["C" "F"]]) ; "A"
  (next-step [["C" "F"] ["C" "A"] ["C" "B"]]) ; "A"
  )
