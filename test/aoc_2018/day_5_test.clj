(ns aoc-2018.day-5-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-5 :as sut]))

(deftest test-reactable?
  (testing "reactable"
    (is (sut/reactable? \a \A))
    (is (sut/reactable? \B \b)))
  (testing "not reactable"
    (is (not (sut/reactable? \a \a)))
    (is (not (sut/reactable? \B \a)))))
