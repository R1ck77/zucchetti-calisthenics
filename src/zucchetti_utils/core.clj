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

;;;;; CALISTHENICS VIOLATION

(defn- is-error? [v]
  (= v :error))

(defn- *-or-error [ & args]
  (if (empty? args)
    1
    (if (some is-error? args)
      :error
      (apply * args))))

(defn- +-or-error [ & args]
    (if (empty? args)
    1
    (if (some is-error? args)
      :error
      (apply + args))))

(defn parse-time-element [s-hours s-minutes]
  (+-or-error (*-or-error 60 (parse-hours s-hours)) (parse-minutes s-minutes)))

(defn parse-time [s]
  (let [splitted (split-time s)]
    (case splitted
      :error :error
      (apply parse-time-element splitted)))  
  )

;;;;;;;;;;;

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
