(ns whd-advent.core
  (:gen-class)
  (:use whd-advent.rooms))

(defn -main
  "Main routine for the application."
  [& args]
  (println "Will's Text Adventure")
  (println)
  (loop []
    (println "Nowhere to be") ; TBD: Should be the local description
    (print "\nWell? ")
    (flush)
    (printf "\nYou said: %s\n" (read-line))
    (recur)))
