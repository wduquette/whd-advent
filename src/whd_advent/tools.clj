;;;; tools.clj
;;;;
;;;; This module contains individual functions for various purposes.

(ns whd-advent.tools
  (:require [clojure.string :as str])
  (:use whd-advent.debug))

;;; ## String Processing
;;;
;;; The following functions are used for string processing, i.e., for
;;; processing commands on input and formatting text for output.

(def ^:dynamic *columns* 72)

(defn wrap-line 
  "Given a number of text columns and a text string, this function rewraps
  the text string to the given column and returns a sequence of the lines.
  [Original Source](http://rosettacode.org/wiki/Word_wrap#Clojure)"
  [size text]
  (loop [left size line [] lines []
         words (clojure.string/split text #"\s+")]
    (if-let [word (first words)]
      (let [wlen (count word)
            spacing (if (== left size) "" " ")
            alen (+ (count spacing) wlen)]
        (if (<= alen left)
          (recur (- left alen) (conj line spacing word) lines (next words))
          (recur (- size wlen) [word] (conj lines (apply str line)) (next words))))
      (when (seq line)
        (conj lines (apply str line))))))


(defn wrap-text
  "Wraps a string to fit a specified number of text columns.  If the width
  is omitted, it defaults to *columns*.  The string may be specified as a
  single string, or as a sequence of strings; the sequence may contain
  nils, which will be ignored."
  ([ss] (wrap-text *columns* ss))
  ([width ss] 
    (let [s (if (string? ss) ss (str/join " " ss))] 
      (str/join "\n" (wrap-line width s)))))

