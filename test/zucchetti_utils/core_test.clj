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
    (is (= (list "13" "45") (split-time "13.45"))))
  (testing "split time returns :error on a rainy day"
    (is (= :error (split-time "1345")))
    (is (= :error (split-time "13.45.00")))))

(deftest test-verify-value
  (testing "returns :error if the input is :error"
    (is (= :error (verify-value :error (constantly true))))
    (is (= :error (verify-value :error (constantly false)))))
  (testing "returns the value if the input applied to the function is true"
    (is (= 12 (verify-value 12 #(= 12 %)))))
  (testing "returns :error if the input applied to the function is false"
    (is (= :error (verify-value 12 #(= 13 %))))))

(deftest test-parse-integer
  (testing "returns the value if the integer is a proper decimal integer"
    (is (= 10 (parse-integer "10")))
    (is (= 0 (parse-integer "00")))
    (is (= 9 (parse-integer "09")))
    (is (= -5 (parse-integer "-5"))))
  (testing "returns :error if the integer is not parsable or not in base 10"
    (is (= :error (parse-integer "0xfa")))
    (is (= :error (parse-integer "fa")))
    (is (= :error (parse-integer "string")))
    (is (= :error (parse-integer "12.3")))))

(deftest test-parse-hours
  (testing "returns the hour on a sunny day"
    (is (= 4 (parse-hours "4")))
    (is (= 16 (parse-hours "16"))))
  (testing "returns :error if the hour is not correct"
    (is (= :error (parse-hours "-4")))
    (is (= :error (parse-hours "25")))
    (is (= :error (parse-hours "a")))))

(deftest test-parse-minutes
  (testing "returns the number of minutes since the start of the hour on sunny day"
    (is (= 4 (parse-minutes "4")))
    (is (= 16  (parse-minutes "16"))))
  (testing "returns :error if the minuts are not correct"
    (is (= :error (parse-minutes "-4")))
    (is (= :error (parse-minutes "60")))
    (is (= :error (parse-minutes "61")))    
    (is (= :error (parse-minutes "a")))))

(deftest test-parse-time-elements
  (testing "parsing a time returns the number of minutes since midnight"
    (is (= 60 (parse-time "1.00")))
    (is (= 67 (parse-time "1.07")))
    (is (= (* 24 60) (parse-time "24.00"))))
  (testing "invalid values return a :error token"
    (is (= :error (parse-time "-1.00")))
    (is (= :error (parse-time "25.00")))
    (is (= :error (parse-time "12.61")))
    (is (= :error (parse-time "12.-40")))
    (is (= :error (parse-time "foobar")))))

(deftest test-compute-intervals
  (testing "returns :error if any element is :error"
    (is (= :error (compute-intervals (list 1 2 3 :error 5 6))))
    (is (= :error (compute-intervals (list 1 2 3 :error 5 6 :error))))
    (is (= :error (compute-intervals (list :error :error :error :error)))))
  (testing "returns :error if the number of elements is odd"
    (is (= :error (compute-intervals (list 1))))
    (is (= :error (compute-intervals (list 1 2 3 4 5)))))
  (testing "returns :error if the values are not increasing"
    (is (= :error (compute-intervals (list 1 5 2 6))))
    (is (= :error (compute-intervals (list 3 2)))))
  (testing "returns 0 if the arguments are empty"
    (is (= 0 (compute-intervals (list)))))
  (testing "returns the correct value on a sunny day"
    (is (= 9 (compute-intervals (list 1 10))))
    (is (= 11 (compute-intervals (list 1 10 13 15))))))

(deftest test-intervals-parsing
  (testing "happy day, single interval correctly parsed"
    (is (= 241 (intervals-parsing "   08.31      12.32"))))
  (testing "happy day, multiple intervals parsed correctly"
    (is (= 466 (intervals-parsing "   08.31      12.32      13.16      17.01   "))))
  (testing "happy day, empty interval parsed correctly"
    (is (zero? (intervals-parsing "   08.31      8.31"))))
  (testing "wrong interval returns :error"
    (is (= :error (intervals-parsing "   08.31      8.12")))))

(deftest test-round-to-working-units
  (testing "approximates correctly 0"
    (is (= 0 (round-to-working-units 0))))
  (testing "approximates correctly less than working-unit minutes"
    (is (= 0 (round-to-working-units 12))))
  (testing "sunny day"
    (is (= 135 (round-to-working-units 145)))))

(deftest test-format-result
  (testing "on a sunny day prints some stats"
    (is (= "Overtime: -5.45 Total: 2.25" (format-result 145)))
    (is (= "Overtime: -7.45 Total: 0.18" (format-result 18)))
    (is (= "Overtime: 2.15 Total: 10.18" (format-result 618))))
  (testing "in case of error, returns a constant message"
    (is (= "Invalid input" (format-result :error)))))
