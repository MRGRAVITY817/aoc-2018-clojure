(ns aoc-2018.day-3-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-3 :as sut]))

(deftest test-parse-claim
  (is (= [{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [1 5]} {:id 1, :coords [1 6]}
          {:id 1, :coords [2 3]} {:id 1, :coords [2 4]} {:id 1, :coords [2 5]} {:id 1, :coords [2 6]}
          {:id 1, :coords [3 3]} {:id 1, :coords [3 4]} {:id 1, :coords [3 5]} {:id 1, :coords [3 6]}
          {:id 1, :coords [4 3]} {:id 1, :coords [4 4]} {:id 1, :coords [4 5]} {:id 1, :coords [4 6]}]
         (sut/parse-claim "#1 @ 1,3: 4x4"))))

(deftest test-parse-and-concat
  (is (= [{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}
          {:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]
         (sut/parse-and-concat "#1 @ 1,3: 2x2\n#2 @ 1,3: 2x2"))))

(deftest test-parse-claims
  (is (= [[{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}]
          [{:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]]
         (sut/parse-claims "#1 @ 1,3: 2x2\n#2 @ 1,3: 2x2"))))

(deftest test-count-overlapping-areas
  (is (= 4
         (sut/count-overlapping-areas [{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}
                                       {:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]))))

(deftest test-isolated-claim
  (testing "every claims are overlapped"
    (is (= nil
           (sut/isolated-claim [[{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}]
                                [{:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]
                                [{:id 3, :coords [5 5]} {:id 3, :coords [1 4]}]]))))
  (testing "has an isolated claim"
    (is (= 3
           (sut/isolated-claim [[{:id 1, :coords [1 3]} {:id 1, :coords [1 4]} {:id 1, :coords [2 3]} {:id 1, :coords [2 4]}]
                                [{:id 2, :coords [1 3]} {:id 2, :coords [1 4]} {:id 2, :coords [2 3]} {:id 2, :coords [2 4]}]
                                [{:id 3, :coords [5 5]}]])))))

