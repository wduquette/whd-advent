;;;; tools.clj
;;;;
;;;; This module contains individual functions for various purposes.

(ns whd-advent.tools
  (:require [clojure.string :as str]))

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
  "Given a number of text columns and a sequence of strings and nils, this 
  function joins the sequence into one string, and then rewraps it to fit
  within the given number of columns.  If the number
  of columns is omitted, it defaults to *columns*."
  ([ss] (wrap-text *columns* ss))
  ([width ss] (str/join "\n" (wrap-line width (str/join " " ss)))))
