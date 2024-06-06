(ns aoc-2018.day-3)

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

(comment
  (parse-claim "#1 @ 1,3: 4x4") ; => [1 1 3 4 4]
  )
