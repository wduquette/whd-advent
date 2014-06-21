(defproject whd-advent "0.0.1-SNAPSHOT"
  :description "Will's Text Adventure"
  :dependencies [
     [org.clojure/clojure "1.6.0"]
     [org.clojure/core.unify "0.5.5"]]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [whd-advent.core]
  :main whd-advent.core)
