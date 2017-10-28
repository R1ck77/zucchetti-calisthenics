(ns zucchetti-utils.core
  (:gen-class))

(defn split-values
  [line]
  (filter (complement empty?)
          (clojure.string/split line #"[\s]")))

(defn validate-tokens [xs]
  )

(defn split-time [s]
  (seq (clojure.string/split s #":")))

(defn parse-time [s]
  
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
