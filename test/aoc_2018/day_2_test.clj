(ns aoc-2018.day-2-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc-2018.day-2 :as sut]))

(deftest test-parse-box-ids
  (is (= (sut/parse-box-ids "abcdef\nbababc\nabbcde\nabcccd\naabcdd\nabcdee\nababab")
         ["abcdef" "bababc" "abbcde" "abcccd" "aabcdd" "abcdee" "ababab"])))

(deftest test-containing-exactly-n-of-any-letter
  (testing "n = 2"
    (is (= (sut/contains-exactly-n-of-any-letter? "abcdef" 2) false))
    (is (= (sut/contains-exactly-n-of-any-letter? "abadef" 2) true)))
  (testing "n = 3"
    (is (= (sut/contains-exactly-n-of-any-letter? "abcdef" 3) false))
    (is (= (sut/contains-exactly-n-of-any-letter? "abadea" 3) true))))

(deftest test-checksum
  (is (= (sut/checksum ["abcdef" "bababc" "abbcde" "abcccd" "aabcdd" "abcdee" "ababab"])
         12)))

(deftest test-char-diff
  (is (= (sut/char-diff "abcde" "axcye") 2))
  (is (= (sut/char-diff "fghij" "fguij") 1)))

(deftest test-similar-box-ids
  (is (= (sut/similar-box-ids ["abcde" "fghij" "klmno" "pqrst" "fguij" "axcye" "wvxyz"])
         ["fghij" "fguij"])))

(deftest test-overlapping-letters
  (is (= (sut/overlapping-letters ["fghij" "fguij"]) "fgij")))

