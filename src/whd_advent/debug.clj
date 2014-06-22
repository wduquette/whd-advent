;;;; debug.clj
;;;;
;;;; Debugging Tools, for use at the REPL.

(ns whd-advent.debug);;; ## Debugging Aids

(defn go 
  "Go to the project namespace whd-advent.<x>, where x can be a string,
  symbol, or keyword."
  [x]
  (in-ns (symbol (str "whd-advent." (name x)))))