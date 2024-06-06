(ns aoc-2018.day-3
  (:require [clojure.string :as str]))

(def claim-regex #"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)")

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
    (for [i (range w)
          j (range h)]
      {:id id :coords [(+ x i) (+ y j)]})))

(defn parse-and-concat
  "Parse and concatenate a list of claims."
  [inputs]
  (mapcat parse-claim inputs))

(defn count-overlapping-areas
  "Count areas that has been overlapped by more than two claims."
  [areas]
  (->> areas
       (map :coords)
       frequencies
       (filter #(>= (val %) 2))
       count))

(defn day-3-part-1
  "Calculate the overlapping area of a list of claims."
  [filename]
  (-> filename
      slurp
      str/split-lines
      parse-and-concat
      count-overlapping-areas))

(comment
  (parse-claim "#1 @ 1,3: 2x2") ; #{{:id 1, :coords [2 3]} {:id 1, :coords [2 4]} {:id 1, :coords [1 4]} {:id 1, :coords [1 3]}}
  (map :coords #{{:id 1, :coords [1 1]} {:id 1, :coords [1 2]} {:id 1, :coords [1 3]} {:id 1, :coords [1 4]}})  ; ([1 2] [1 1] [1 4] [1 3])
  (into #{} #{[1 2] [2 3]}) ; #{1 2 3}
  (->>  [{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}
         {:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]
        (map :coords)
        frequencies
        (filter #(>= (val %) 2)))
  (day-3-part-1 "resources/day_3_input.txt") ; 104241
  )




