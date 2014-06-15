;;;; tools.clj
;;;;
;;;; This module contains individual functions for various purposes.

(ns whd-advent.tools
  (:require [clojure.string :as str]))

;;; ## String Processing
;;;
;;; The following functions are used for string processing, i.e., for
;;; processing commands on input and formatting text for output.

(defn wrap-line 
  "Given a number of text columns and a text string, this function rewraps
  the text string to the given column and returns a sequence of the lines.
  The function was copied from: http://rosettacode.org/wiki/Word_wrap#Clojure"
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
  "Given a number of text columns and a text string, this function rewraps
  the string to the given column, and returns the new string."
  [size text]
  (str/join "\n" (wrap-line size text)))
