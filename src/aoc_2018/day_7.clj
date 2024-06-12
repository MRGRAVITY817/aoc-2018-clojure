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

;; how to solve part 2?
;; - manage a vector that has 5 slots (for each worker), containing remaining seconds
;; - each seconds will be decremented for every recursive call, while we accumulate the total seconds at the same time
;; - if a worker has 0 seconds left, it will be available for the next task (idle)
;; - idle workers will be assigned to the next task (lexicographically first & doesn't have any preceding steps)
;; - if all workers are idle, it means we have finished all tasks

;; loop states
;; - deps and steps
;; - remaining seconds for each worker (5 slots)
;; - steps which are being worked on by each worker
;; - total seconds
;; - done steps

(defn get-finished-step?
  "Get finished step (which has only one remaning second), from given remaining seconds and steps status."
  [remaining status]
  (->> (map vector remaining status)
       (filter #(= 1 (first %)))
       (first)
       (second)))

(defn after-a-second
  "Update three lists (remaining, status, done) after a second has passed."
  [remaining status done]
  (let [finished-step? (get-finished-step? remaining status)]
    {:remaining (apply vector (map #(max 0 (dec %)) remaining))
     :status    (if finished-step?
                  (apply vector (map #(if (= % finished-step?) nil %) status))
                  status)
     :done      (if finished-step?
                  (conj done finished-step?)
                  done)}))

(defn schedule-5-workers
  [deps steps]
  (loop [total-seconds     0
         remaining         [0 0 0 0 0]
         status            [nil nil nil nil nil]
         done              []
         steps             steps]
    (let [{:keys [remaining status done]} (after-a-second remaining status done)])))

(defn idle-worker
  "Find the first idle worker's index from given remaining seconds list.

   For example, if given [1 0 2 3 0], we have worker 1 and worker 4 being idle,
   hence we return the first index 1. 
  "
  [remaining]
  (->> remaining
       (map-indexed vector)
       (filter #(zero? (second %)))
       (ffirst)))

(comment
  (if "B"
    (map #(if (= % "B") nil %) [nil "B" "C" "D" "E"])
    [nil "B" "C" "D" "E"])
  (->> (map #(vector %1 %2) [0 1 2 3 5]
            [nil "B" "C" "D" "E"])
       (filter #(= 1 (first %)))
       (first)
       (second))
  (map #(vector %1 %2) [1 2 3] [1 2 3])
  (day-7-part-1 "resources/day_7_sample.txt") ; "CABDFE"
  (day-7-part-1 "resources/day_7_input.txt")  ; "OCPUEFIXHRGWDZABTQJYMNKVSL"

  (let [{:keys [deps steps]} (->> "resources/day_7_sample.txt"
                                  (slurp)
                                  (string/split-lines)
                                  (map parse-line)
                                  (lines->deps-and-steps))]
; {:deps
;  {"A" #{"C"}, "F" #{"C"}, "B" #{"A"}, "D" #{"A"}, "E" #{"F" "B" "D"}},
;  :steps #{"E" "C" "F" "B" "A" "D"}}
    (order-steps deps steps)))






