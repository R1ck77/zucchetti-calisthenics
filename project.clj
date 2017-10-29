(defproject zucchetti-utils "0.1.0-SNAPSHOT"
  :description "Calisthenics based on a real-life problem's solution"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot zucchetti-utils.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
