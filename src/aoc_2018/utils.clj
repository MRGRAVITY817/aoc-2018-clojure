(ns aoc-2018.utils)

(defn partition-linked
  "Partition collection into linked pairs.

   For example:
   ```
   [1 2 3 4 ...] => [(1 2) (2 3) (3 4) ...]
   ```
   "
  [coll]
  (loop [records coll
         partitions []]
    (if (= (count records) 1)
      partitions
      (recur (rest records)
             (conj partitions (take 2 records))))))

(defn most-frequent
  "Return the most frequent element from given collection."
  [coll]
  (->> coll
       frequencies
       (apply max-key val)
       key))

(comment
  (partition-linked [1 2 3 4 5]) ; [(1 2) (2 3) (3 4) (4 5)]
  (most-frequent [5 2 6 1 7 2 8 1 30 1]) ; 1
  )
