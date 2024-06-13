(ns aoc-2018.day-7
  (:require [clojure.string :as string]
            [clojure.set :refer [subset?]]))

;; Part 1

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

;; Part 2

(defn- find-idle-workers
  "Return the indexes of workers that are idle."
  [remaining]
  (->> remaining
       (map-indexed vector)
       (filter #(zero? (second %)))
       (map first)))

(comment
  (find-idle-workers  [0 0 0 0 0])) ; (0 1 2 3 4)

(defn- find-available-steps
  "Return the steps that are available to be worked on."
  [done status deps steps]
  (->> steps (filter #(and (not (done %))
                           (not (some #{%} (set status)))
                           (subset? (deps %) (set done))))
       (sort)))

(comment
  (find-available-steps #{"C"}
                        [nil "A" nil nil nil]
                        {"A" #{"C"}, "F" #{"C"}, "B" #{"A"}, "D" #{"A"}, "E" #{"F" "B" "D"}}
                        #{"C" "F" "B" "A" "D" "E"}))  ; ("F")

(defn update-status
  "Update the status of workers based on the steps they are working on."
  [idle-workers available-steps status]
  (->> {:idle-workers idle-workers
        :available-steps available-steps
        :status status}
       (iterate (fn [{:keys [idle-workers available-steps status]}]
                  (let [worker (first idle-workers)
                        step   (first available-steps)]
                    {:idle-workers (rest idle-workers)
                     :available-steps (rest available-steps)
                     :status (assoc status worker step)})))
       (take (inc (min (count idle-workers)
                       (count available-steps))))
       (last)
       (:status)))

(comment
  (update-status [1 3 4] ["A" "F"] [nil nil nil nil nil]) ; [nil "A" nil "F" nil]
  (update-status [] ["A" "F"] [nil nil nil nil nil]) ; [nil nil nil nil nil]
  )

(defn get-seconds
  [step offset]
  (+ 1 offset (- (int (first step)) (int \A))))

(comment
  (get-seconds "A" 0)  ; 1
  (get-seconds "F" 0)  ; 6
  (get-seconds "Z" 0)  ; 26
  (get-seconds "A" 60) ; 61
  (get-seconds "F" 60) ; 66
  (get-seconds "Z" 60) ; 86
  )

(defn update-remaining
  "Update the remaining seconds for each worker."
  [idle-workers available-steps remaining offset]
  (->> {:idle-workers idle-workers
        :available-steps available-steps
        :remaining remaining}
       (iterate (fn [{:keys [idle-workers available-steps remaining]}]
                  (let [worker (first idle-workers)
                        step   (first available-steps)]
                    {:idle-workers (rest idle-workers)
                     :available-steps (rest available-steps)
                     :remaining (assoc remaining worker (get-seconds step offset))})))
       (take (inc (min (count idle-workers)
                       (count available-steps))))
       (last)
       (:remaining)))

(comment
  (update-remaining [1 3 4] ["A" "F"] [0 0 0 0 0] 0) ; [0 1 0 6 0]
  (update-remaining [] ["A" "F"] [0 0 0 0 0] 0) ; [0 0 0 0 0]
  (update-remaining [1 3 4] ["A" "F" "Z"] [2 0 1 0 0] 60) ; [2 61 1 66 86]
  )

(defn update-done
  "Update the steps that have been completed.
   Completed steps are the ones:
   - that have been worked on by workers
   - remaining seconds for those steps are 0
   "
  [done status remaining]
  (->> (map vector status remaining)
       (filter #(and (zero? (second %))
                     (identity (first %))))
       (map first)
       (into done)))

(comment
  (update-done #{"C"} [nil "A" nil "F" nil] [0 0 0 0 0])  ; #{"C" "F" "A"}
  )

(defn after-one-second
  "Update the status of workers, remaining seconds, and done steps after one second."
  [remaining status done]
  (let [new-remaining (map #(max (dec %) 0) remaining)
        new-status (map (fn [r s] (if (zero? r) nil s)) new-remaining status)]
    {:remaining (into [] new-remaining)
     :status (into [] new-status)
     :done (update-done done status new-remaining)}))

(comment
  (after-one-second [0 0 0 0 0] [nil "A" nil "F" nil] #{})
; {:remaining [0 0 0 0 0],
;  :status [nil nil nil nil nil],
;  :done #{"F" "A"}}
  )

(defn iter-fn
  [{:keys [seconds done deps steps remaining status]} offset]
  (let [{:keys [remaining status done]} (after-one-second remaining status done)
        idle-workers (find-idle-workers remaining)
        available-steps (find-available-steps done status deps steps)
        new-status (update-status idle-workers available-steps status)
        new-remaining (update-remaining idle-workers available-steps remaining offset)
        new-seconds (inc seconds)]
    {:seconds new-seconds
     :done done
     :deps deps
     :steps steps
     :remaining new-remaining
     :status new-status}))

(defn stop-fn
  [done initial-steps]
  (not= done initial-steps))

(defn schedule-workers
  [worker-count offset deps steps]
  (->> {:seconds 0
        :done #{}
        :deps deps
        :steps steps
        :remaining (into [] (repeat worker-count 0))
        :status (into [] (repeat worker-count nil))}
       (iterate #(iter-fn % offset))
       (take-while #(stop-fn (:done %) steps))
       (last)
       (:seconds)))

(comment
  (let [{:keys [deps steps]} (->> "resources/day_7_sample.txt"
                                  (slurp)
                                  (string/split-lines)
                                  (map parse-line)
                                  (lines->deps-and-steps))]
    (schedule-workers 2 0 deps steps)) ; 15

  (let [{:keys [deps steps]} (->> "resources/day_7_input.txt"
                                  (slurp)
                                  (string/split-lines)
                                  (map parse-line)
                                  (lines->deps-and-steps))]
    (schedule-workers 5 60 deps steps)) ; 991

  (day-7-part-1 "resources/day_7_sample.txt") ; "CABDFE"
  (day-7-part-1 "resources/day_7_input.txt")  ; "OCPUEFIXHRGWDZABTQJYMNKVSL"

  (let [{:keys [deps steps]} (->> "resources/day_7_sample.txt"
                                  (slurp)
                                  (string/split-lines)
                                  (map parse-line)
                                  (lines->deps-and-steps))]
    (order-steps deps steps)))


