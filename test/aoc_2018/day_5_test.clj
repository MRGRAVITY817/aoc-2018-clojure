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

(deftest test-react
  (testing "react"
    (is (= [] (sut/react "aA")))
    (is (= [] (sut/react "abBA")))
    (is (= [\a \b \A \B] (sut/react "abAB")))
    (is (= [\d \a \b \C \B \A \c \a \D \A] (sut/react "dabAcCaCBAcCcaDA")))))
