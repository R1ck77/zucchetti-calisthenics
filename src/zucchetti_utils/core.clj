(ns zucchetti-utils.core
  (:gen-class))

(def ^:const minutes-in-hour 60)
(def ^:const work-unit 15)
(def ^:const working-hours 8)

(defn split-values
  [line]
  (filter (complement empty?)
          (clojure.string/split line #"[\s]")))

(defn parse-integer
  "Boundary function, so calisthenics exempt"
  [s]
  (try
    (Integer/valueOf s)
    (catch NumberFormatException e :error)))

(defn verify-value [value f]
  (case value
    :error :error
    (if (f value)
      value
      :error)))

(defn- less-or-equal-than-this? [value]
  (partial <= value))

(defn- greater-or-equal-than-this? [value]
  (partial >= value))

(defn- verify-my-value [value]
  (partial verify-value value))

(defn- verify-no-less-than [min value]
  ((verify-my-value value) (less-or-equal-than-this? min)))

(defn- verify-no-more-than [max value]
  ((verify-my-value value) (greater-or-equal-than-this? max)))

(defn- verify-no-less-than-this [min]
  (partial verify-no-less-than min))

(defn- verify-no-more-than-this [max]
  (partial verify-no-more-than max))

(defn- convert-time [{s :value
                   min-value :min
                   max-value :max}]
  ((verify-no-more-than-this max-value)
   ((verify-no-less-than-this min-value)
    (parse-integer s))))

(defn parse-hours [s]
  (convert-time {:value s :min 0 :max 24}))

(defn parse-minutes [s]
  (convert-time {:value s :min 0 :max 59}))

(defn- is-error? [v]
  (= v :error))

(defn- error-present [s]
  (some is-error? s))

(defn- f-or-error [f & args]
  (if (empty? args)
    1
    (if (error-present args)
      :error
      (apply f args))))

(def ^:private f-or-error-on-* (partial f-or-error *))

(def ^:private apply-*-or-error (partial apply f-or-error-on-*))

(defn- *-or-error [ & args]
  (apply-*-or-error args))

(def ^:private f-or-error-on-+ (partial f-or-error +))

(def ^:private apply-+-or-error (partial apply f-or-error-on-+))

(defn- +-or-error [ & args]
  (apply-+-or-error args))

(defn- add-to-this-if-not-error [value]
  (partial +-or-error value))

(defn- multiply-by-this-if-not-error [value]
  (partial *-or-error value))

(defn- convert-time-element-to-minutes [s-hours s-minutes]
  ((add-to-this-if-not-error ((multiply-by-this-if-not-error minutes-in-hour) (parse-hours s-hours))) (parse-minutes s-minutes)))

(defn- create-time-conversion-function-for-hour [s-hour]
  (partial convert-time-element-to-minutes s-hour))

(defn- convert-time-elements-to-minutes [xs]
  ((create-time-conversion-function-for-hour (first xs)) (second xs)))

(defn validate-tokens [xs]
  (case (count xs)
    2 xs
    :error))

(defn split-time [s]
  (validate-tokens (seq (clojure.string/split s #"[.]"))))

(defn- or-error [f value]
  (case value
    :error :error
    (f value)))

(def ^:private convert-time-elements-to-minutes-or-error  (partial or-error convert-time-elements-to-minutes))

(defn parse-time [s]
  (convert-time-elements-to-minutes-or-error (split-time s)))

(def ^:private apply-neg (partial apply -))

(defn- sum-interval [xn]
  (- (apply-neg xn)))

(defn- sum-intervals [xn]
  (map sum-interval (partition 2 2 xn)))

(defn- safe-compute-intervals [xn]
  {:pre [(not (empty? xn))]}
   (apply + (sum-intervals xn)))

(defn- compare-value-with-accumulator [{error :error last :last} value]
  (cond
    error {:error error :last last}
    (>= value last) {:error error :last value}
    :default {:error true :last value}))

(def ^:private accumulate-trending-errors (partial reduce compare-value-with-accumulator))

(defn- reduce-compare-with-accumulator [acc]
  (partial accumulate-trending-errors acc))

(defn- reduce-compare-with-accumulator-for-zero-accumulator [xn]
  ((reduce-compare-with-accumulator {:error false :last (first xn)}) (rest xn)))

(defn- increasing?
  "Helper function, not for generic use"
  [xn]
  {:pre [(not (empty? xn))]}
  (not ((reduce-compare-with-accumulator-for-zero-accumulator xn) :error)))

(defn compute-intervals [xn]
  (cond 
    (odd? (count xn)) :error
    (empty? xn) 0
    (some is-error? xn) :error
    (not (increasing? xn)) :error
    :default (safe-compute-intervals xn)))

(def ^:private parse-all-times (partial map parse-time))

(defn intervals-parsing [s]
  (compute-intervals (parse-all-times (split-values s))))

(defn- abs [n]
  (java.lang.Math/abs n))

(defn- format-minutes [n]
  (format "%02d" (abs n)))

(defn- format-hours [n]
  (str (int n)))

(defn- format-components [hm]
  (str (format-hours (first hm)) "." (format-minutes (second hm))))

(defn- remaining-minutes [n]
  (rem n minutes-in-hour))

(defn- convert-minutes
  "Returns a tuple of hours/minutes

The minutes will be negative if the hours are!"
  [n]
  (let [minutes (remaining-minutes n)]
    (list (/ (- n minutes) minutes-in-hour) minutes)))

(defn round-to-working-units [n]
  {:pre [(not (neg? n))]}
  (- n (rem n work-unit)))

(defn- format-time [n]
  (format "Overtime: %s Total: %s"
          (format-components (convert-minutes (- (round-to-working-units n) (* working-hours minutes-in-hour))))
          (format-components (convert-minutes n))))

(defn format-result [v]
  (if (= v :error)
    "Invalid input"
    (format-time v)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
