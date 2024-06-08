(ns aoc-2018.day-3
  (:require [clojure.string :as str]
            [clojure.set :refer [subset?]]
            [malli.core :as m]
            [malli.dev :as mdev]))

(mdev/start!)

(def Coordinate
  [:vector :int])

(def Claim
  [:sequential
   [:map
    [:id :int]
    [:coords Coordinate]]])

(def claim-regex #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")

(m/=> parse-claim [:=> [:cat :string] [:maybe Claim]])
(defn parse-claim
  "Parse a claim from a string and return a set of coordinates.
   Example:
   ```
   (parse-claim \"#1 @ 1,3: 4x4\") ; => #{{:id 1 :coords [1 3]} ... }
   ```
   "
  [input]
  (let [[id x y w h] (->> input
                          (re-matches claim-regex)
                          rest
                          (mapv #(Integer/parseInt %)))]
    (when (and id x y w h)
      (for [i (range w)
            j (range h)]
        {:id id :coords [(+ x i) (+ y j)]}))))

(m/=> parse-and-concat [:=> [:cat :string] Claim])
(defn parse-and-concat
  "Parse and concatenate a list of claims."
  [input]
  (->> input
       str/split-lines
       (mapcat parse-claim)))

(m/=> count-overlapping-areas [:=> [:cat Claim] :int])
(defn count-overlapping-areas
  "Count areas that has been overlapped by more than two claims."
  [areas]
  (->> areas
       (map :coords)
       frequencies
       (filter #(>= (val %) 2))
       count))

(m/=> day-3-part-1 [:=> [:cat :string] :int])
(defn day-3-part-1
  "Calculate the overlapping area of a list of claims."
  [filename]
  (-> filename slurp parse-and-concat ;; parsing
      count-overlapping-areas))       ;; counting

(m/=> isolated-coords [:=> [:cat [:sequential Claim]] [:set Coordinate]])
(defn- isolated-coords
  "Get the coordinates that are not overlapped by any other claims."
  [claims]
  (->> claims
       flatten
       (map :coords)
       frequencies
       (filter #(= (val %) 1))
       keys
       set))

(m/=> isolated-claim [:=> [:cat [:sequential Claim]] [:maybe :int]])
(defn isolated-claim
  "Find and return the id of first claim that is not overlapped by any other claims."
  [claims]
  (let [isolated-coords (isolated-coords claims)]
    (->> claims
         (filter #(subset? (set (map :coords %)) isolated-coords))
         flatten
         (map :id)
         first)))

(m/=> parse-claims [:=> [:cat :string] [:sequential Claim]])
(defn parse-claims
  "Parse a list of claims from a string."
  [input]
  (->> input
       str/split-lines
       (map parse-claim)
       (filter identity)))

(m/=> day-3-part-2 [:=> [:cat :string] :int])
(defn day-3-part-2
  "Find the id of the claim that is not overlapped by any other claims."
  [filename]
  (-> filename slurp parse-claims ;; parsing
      isolated-claim))            ;; finding

(comment
  (day-3-part-1 "resources/day_3_input.txt") ; 104241
  (day-3-part-2 "resources/day_3_input.txt") ; 806
  ;;

  (->> "#1 @ 1,3: 2x2"
       parse-claim)
  (map :coords #{{:id 1, :coords [1 1]} {:id 1, :coords [1 2]} {:id 1, :coords [1 3]} {:id 1, :coords [1 4]}})  ; ([1 2] [1 1] [1 4] [1 3])
  (into #{} #{[1 2] [2 3]}) ; #{1 2 3}
  (->>  [{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}
         {:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]
        (map :coords)
        frequencies
        (filter #(>= (val %) 2)))
  (->> (map :coords [{:id 1, :coords [1 2]} {:id 1, :coords [1 3]}]) ; ([1 2] [1 3])
       (every? #(contains? [[1 2] [1 3] [2 1] [2 2]] %)))
  (subset? #{[1 2] [1 3]} #{[1 2] [1 3] [2 1] [2 2]}) ; true
  (->> [[{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}]
        [{:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]
        [{:id 3, :coords [5 5]}]]
       flatten
       (map :coords)
       frequencies
       (filter #(= (val %) 1))
       keys
       set)
  (->> [[{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}]
        [{:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]
        [{:id 3, :coords [5 5]}]]
       (filter #(subset? (set (map :coords %)) #{[5 5]}))
       flatten
       #_(map :id)
       #_first))

