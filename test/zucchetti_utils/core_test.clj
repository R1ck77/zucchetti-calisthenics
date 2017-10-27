(ns zucchetti-utils.core-test
  (:require [clojure.test :refer :all]
            [zucchetti-utils.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))


(deftest test-interval-parsing
  (testing "happy day, single interval correctly parsed"
    "   08.31      12.32")
  (testing "happy day, multiple intervals parsed correctly"
    "   08.31      12.32      13.16      17.01   ")
  (testing "happy day, empty interval parsed correctly"
    "   08.31      8.31")
  (testing "wrong interval throws an exception"
    "   08.31      8.12"))
