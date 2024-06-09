(ns aoc-2018.day-5-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-5 :as sut]
            [malli.core :as m]))

(deftest test-reactable?
  (testing "schema check"
    (is (m/validate [:=> [:catn
                          [:a :symbol]
                          [:b :symbol]] :boolean]
                    sut/reactable?)))
  (testing "reactable"
    (is (sut/reactable? \a \A))
    (is (sut/reactable? \B \b)))
  (testing "not reactable"
    (is (not (sut/reactable? \a \a)))
    (is (not (sut/reactable? \B \a)))))

(deftest test-react
  (testing "schema check"
    (is (m/validate [:function
                     [:=> [:cat :string] [:vector :symbol]]
                     [:=> [:catn
                           [:acc [:vector :symbol]]
                           [:chars :string]] [:vector :symbol]]]
                    sut/react)))
  (testing "react"
    (is (= [] (sut/react "aA")))
    (is (= [] (sut/react "abBA")))
    (is (= [\a \b \A \B] (sut/react "abAB")))
    (is (= [\d \a \b \C \B \A \c \a \D \A] (sut/react "dabAcCaCBAcCcaDA")))))

(deftest test-remove-alphabet
  (testing "schema check"
    (is (m/validate [:=> [:cat :string :symbol] :string]
                    sut/remove-alphabet)))
  (testing "remove \\a/\\A"
    (is (= "bB"
           (sut/remove-alphabet "abBA" \a)))
    (is (= "dbcCCBcCcD"
           (sut/remove-alphabet "dabAcCaCBAcCcaDA" \a)))))

