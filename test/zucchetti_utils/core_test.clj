(ns zucchetti-utils.core-test
  (:require [clojure.test :refer :all]
            [zucchetti-utils.core :refer :all]))

(deftest test-split-values
  (testing "a value without padding is correctly extracted"
    (is (= (list "foobar") (split-values "foobar"))))
  (testing "padding is not an issue either"
    (is (= (list "foobar") (split-values "   foobar     "))))
  (testing "multiple intervals with padding are also correctly returned"
    (is (= (list "foo" "bar" "baz") (split-values "   foo     bar baz     ")))))

(deftest test-validate-tokens
  (testing "returns the tokens on a sunny day"
    (is (= (list 13 45) (validate-tokens (list 13 45)))))
  (testing "returns error for bogus values"
    (is (= :error (validate-tokens (list 13 14 12))))
    (is (= :error (validate-tokens (list 13))))))

(deftest test-split-time
  (testing "split time returns a sequence with 2 strings on a happy day"
    (is (= (list "13" "45") (split-time "13:45"))))
  (testing "split time returns :error on a rainy day"
    (is (= :error (split-time "1345")))
    (is (= :error (split-time "13:45:00")))))

(deftest test-parse-time
  (testing "parsing a time returns the number of minutes since midnight"
    (is (= 60 (parse-time "1:00")))
    (is (= 67 (parse-time "1:07")))
    (is (= (* 24 60) (parse-time "24:00"))))
  (testing "invalid values return a :error token"
    (is (= :error (parse-time "-1:00")))
    (is (= :error (parse-time "25:00")))
    (is (= :error (parse-time "12:61")))
    (is (= :error (parse-time "12:-40")))
    (is (= :error (parse-time "foobar")))))

(deftest test-interval-parsing
  (testing "happy day, single interval correctly parsed"
    "   08.31      12.32")
  (testing "happy day, multiple intervals parsed correctly"
    "   08.31      12.32      13.16      17.01   ")
  (testing "happy day, empty interval parsed correctly"
    "   08.31      8.31")
  (testing "wrong interval throws an exception"
    "   08.31      8.12"))
