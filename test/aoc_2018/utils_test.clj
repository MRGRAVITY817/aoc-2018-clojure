(ns aoc-2018.utils-test
  (:require [clojure.test :refer [deftest is]]
            [aoc-2018.utils :as sut]))

(deftest test-partition-linked
  (is (=  '[(1 2) (2 3) (3 4) (4 5)]
          (sut/partition-linked [1 2 3 4 5]))))

(deftest test-most-frequent
  (is (= 1 (sut/most-frequent [5 2 6 1 7 2 8 1 30 1])))
  (is (= {:guard 1, :minute 2}
         (sut/most-frequent [{:guard 1, :minute 2}
                             {:guard 1, :minute 2}
                             {:guard 1, :minute 3}]))))

