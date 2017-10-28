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

(deftest test-parse-time
  (testing "times are correctly parsed"))

(deftest test-interval-parsing
  (testing "happy day, single interval correctly parsed"
    "   08.31      12.32")
  (testing "happy day, multiple intervals parsed correctly"
    "   08.31      12.32      13.16      17.01   ")
  (testing "happy day, empty interval parsed correctly"
    "   08.31      8.31")
  (testing "wrong interval throws an exception"
    "   08.31      8.12"))
