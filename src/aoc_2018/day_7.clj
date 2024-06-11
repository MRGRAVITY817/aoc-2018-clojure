(ns aoc-2018.day-7
  (:require [clojure.string :as string]
            [clojure.set :refer [subset?]]))

(def re #"Step (\w) must be finished before step (\w) can begin\.")

(defn parse-line
  "Parse a line that describes a step dependency.
   Returns a vector of two elements: 
    - the step that must be finished before the other step, 
    - and the step that can begin after the first step is finished.
  "
  [line]
  (let [[_ current next] (re-find re line)]
    {:current current :next next}))

(defn lines->deps-and-steps
  "Given a list of lines that describe step dependencies, return a map of dependencies and steps.
   The map contains two keys:
   - deps: a map of steps and their dependencies
   - steps: a set of all steps that are required to be completed."
  [lines]
  (loop [lines lines
         deps  {}
         steps #{}]
    (if (empty? lines)
      {:deps deps :steps steps}
      (let [parsed  (first lines)
            current (:current parsed)
            next    (:next parsed)]
        (recur (rest lines)
               (assoc deps next (conj (deps next #{}) current))
               (conj steps current next))))))

(defn order-steps
  "Return the order of steps that must be taken to complete all steps."
  [deps steps]
  (loop [done  []
         steps steps]
    (if (empty? steps)
      done
      (let [candidates (filter #(subset? (deps %) (set done)) steps)
            head       (first (sort candidates))]
        (recur (conj done head)
               (disj steps head))))))

(defn day-7-part-1
  [filename]
  (let [{:keys [deps steps]} (->> filename
                                  (slurp)
                                  (string/split-lines)
                                  (map parse-line)
                                  (lines->deps-and-steps))]
    (->> (order-steps deps steps)
         (apply str))))

(comment
  (day-7-part-1 "resources/day_7_sample.txt") ; "CABDFE"
  (day-7-part-1 "resources/day_7_input.txt")  ; "OCPUEFIXHRGWDZABTQJYMNKVSL"

  (let [{:keys [deps steps]} (->> "resources/day_7_sample.txt"
                                  (slurp)
                                  (string/split-lines)
                                  (map parse-line)
                                  (lines->deps-and-steps))]
    (order-steps deps steps)))

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

(defn step-set
  "Get a set of all steps from the given connections (graph).

   For example, given a set of connections `#{[\"C\" \"A\"] [\"C\" \"F\"] [\"F\" \"E\"]}`,
   we return a set of steps #{\"A\" \"C\" \"E\" \"F\"}.
  "
  [connections]
  (->> connections
       (mapcat identity)
       (set)))

#_(defn day-7-part-1
    "Return the order of steps that must be taken to complete all steps."
    [filename]
    (let [connections (->> filename
                           (slurp)
                           (string/split-lines)
                           (map parse-line))
          steps       (step-set connections)
          order       []]
      (loop [order order
             steps steps
             connections connections]
        (if (empty? steps)
          order
          (let [head (graph-head connections)
                next (next-step connections)]
            (recur (conj order head)
                   (disj steps head)
                   (remove #(= [head next] %) connections)))))))

(comment
  #_(day-7-part-1 "resources/day_7_sample.txt")
  #_(day-7-part-1 "resources/day_7_input.txt")
  ;; Helper functions
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




