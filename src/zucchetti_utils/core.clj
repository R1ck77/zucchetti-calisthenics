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

(defn parse-integer [s]
  (try
    (Integer/valueOf s)
    (catch NumberFormatException e :error)))

(defn parse-time [{s :value
                   min-value :min
                   max-value :max}]
  )

(defn parse-hours [s]
  
  )

(defn parse-minutes [s]
  )

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
