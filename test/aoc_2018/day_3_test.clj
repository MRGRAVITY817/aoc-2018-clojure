(ns aoc-2018.day-3-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-3 :as sut]))

(deftest test-parse-claim
  (is (= #{{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [1 5]} {:id 1, :coords [1 6]}
           {:id 1, :coords [2 3]} {:id 1, :coords [2 4]} {:id 1, :coords [2 5]} {:id 1, :coords [2 6]}
           {:id 1, :coords [3 3]} {:id 1, :coords [3 4]} {:id 1, :coords [3 5]} {:id 1, :coords [3 6]}
           {:id 1, :coords [4 3]} {:id 1, :coords [4 4]} {:id 1, :coords [4 5]} {:id 1, :coords [4 6]}}
         (sut/parse-claim "#1 @ 1,3: 4x4"))))

(deftest test-overlapping-area
  (is (= #{[1 1] [1 2]}
         (sut/overlapping-area #{{:id 1, :coords [1 1]} {:id 1, :coords [1 2]}
                                 {:id 1, :coords [1 3]} {:id 1, :coords [1 4]}}
                               #{{:id 2, :coords [1 1]} {:id 2, :coords [1 2]}
                                 {:id 2, :coords [2 1]} {:id 2, :coords [2 2]}}))))



