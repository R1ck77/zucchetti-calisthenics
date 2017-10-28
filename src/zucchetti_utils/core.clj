(ns zucchetti-utils.core
  (:gen-class))

(defn split-values
  [line]
  (filter (complement empty?)
          (clojure.string/split line #"[\s]")))

(defn validate-tokens [xs]
  (case (count xs)
    2 xs
    :error))

(defn split-time [s]
  (validate-tokens (seq (clojure.string/split s #":"))))

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

(defn- *-or-error [ & args]
  (apply (partial f-or-error *) args))

(defn- +-or-error [ & args]
  (apply (partial f-or-error +) args))

(defn- add-to-this-if-not-error [value]
  (partial +-or-error value))

(defn- multiply-by-this-if-not-error [value]
  (partial *-or-error value))

(defn- convert-time-element-to-minutes [s-hours s-minutes]
  ((add-to-this-if-not-error ((multiply-by-this-if-not-error 60) (parse-hours s-hours))) (parse-minutes s-minutes)))

(defn- create-time-conversion-function-for-hour [s-hour]
  (partial convert-time-element-to-minutes s-hour))

(defn- convert-time-elements-to-minutes [xs]
  ((create-time-conversion-function-for-hour (first xs)) (second xs)))

;;;;; CALISTHENICS VIOLATION


(defn parse-time [s]
  (let [splitted (split-time s)]
    (case splitted
      :error :error
      (convert-time-elements-to-minutes splitted)))  
  )

;;;;;;;;;;;

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
