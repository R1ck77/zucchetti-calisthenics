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

;;;;; CALISTHENICS VIOLATION
(defn verify-no-less-than [value min]
  (verify-value value #(>= % min)))

(defn verify-no-more-than [value max]
    (verify-value value #(<= % max)))

(defn convert-time [{s :value
                   min-value :min
                   max-value :max}]
  (verify-no-more-than
   (verify-no-less-than
    (parse-integer s) min-value) max-value))

(defn parse-hours [s]
  (convert-time {:value s :min 0 :max 24}))

(defn parse-minutes [s]
  (convert-time {:value s :min 0 :max 59}))

;;;;;;;;;;;;;;;;;

(defn parse-time-elements [sh sm]
  )

(defn parse-time [s]
  (let [splitted (split-time s)]
    (case splitted
      :error :error
      (apply parse-time-elements splitted)))
  
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
