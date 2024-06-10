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

(defn no-previous-steps?
  "Given a step and a set of connections (graph), return true if the step has any preceding steps.
   Otherwise, return false.
  "
  [step connections]
  (not-any? (fn [conn] (= step (second conn))) connections))

(defn graph-head
  "Given a set of connections (graph), return the step that... 
   - has no previous steps
   - and alphabetically comes first.
    
   For example, when given a list of connections `#{[\"C\" \"A\"] [\"C\" \"F\"] [\"F\" \"E\"]}`,
   - 'C' and 'F' has no previous steps
   - 'C' comes first alphabetically.
   ... so we return 'C'.
  "
  [connections]
  (->> connections
       (map first)
       (filter #(no-previous-steps? % connections))
       (sort)
       (first)))

(comment
  (parse-line "Step C must be finished before step A can begin.") ; ["C" "A"]
  (next-step [["C" "A"]]) ; "A"
  (next-step [["C" "A"] ["C" "F"]]) ; "A"
  (next-step [["C" "F"] ["C" "A"] ["C" "B"]]) ; "A"
  (no-previous-steps? "C" #{["C" "A"] ["C" "F"]}) ; true
  (no-previous-steps? "A" #{["C" "A"] ["C" "F"]}) ; false
  (graph-head #{["C" "A"] ["C" "F"]}) ; "C"
  (graph-head #{["C" "F"] ["C" "A"] ["F" "E"]}) ; "C"
  (graph-head #{["C" "A"] ["C" "F"] ["A" "B"] ["A" "D"] ["B" "E"] ["D" "E"] ["F" "E"]}) ; "C"
  )
