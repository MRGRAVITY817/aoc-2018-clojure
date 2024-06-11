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


