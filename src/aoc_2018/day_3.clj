(ns aoc-2018.day-3
  (:require [clojure.set :refer [intersection]]))

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
    (set (for [i (range w)
               j (range h)]
           {:id id :coords [(+ x i) (+ y j)]}))))

(defn overlapping-area
  "Find the overlapping area between two sets of coordinates."
  [area1 area2]
  (let [coords1 (->> area1 (map :coords) set)
        coords2 (->> area2 (map :coords) set)
        overlapping (intersection coords1 coords2)]
    overlapping))

(comment
  (parse-claim "#1 @ 1,3: 2x2") ; #{{:id 1, :coords [2 3]} {:id 1, :coords [2 4]} {:id 1, :coords [1 4]} {:id 1, :coords [1 3]}}
  (map :coords #{{:id 1, :coords [1 1]} {:id 1, :coords [1 2]} {:id 1, :coords [1 3]} {:id 1, :coords [1 4]}})  ; ([1 2] [1 1] [1 4] [1 3])
  (overlapping-area #{{:id 1, :coords [1 1]} {:id 1, :coords [1 2]} {:id 1, :coords [1 3]} {:id 1, :coords [1 4]}}
                    #{{:id 2, :coords [1 1]} {:id 2, :coords [1 2]} {:id 2, :coords [2 1]} {:id 2, :coords [2 2]}})  ; #{[1 1] [1 2]}
  )
