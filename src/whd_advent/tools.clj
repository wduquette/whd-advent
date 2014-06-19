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

(defn join-text
  "Given a separator and a sequence of strings and nils, joins the 
  non-nil elements of the sequence using the separator."
  [sep ss]
  (str/join sep (filter  (fn [x] (not (nil? x))) ss)))

(defn wrap-text
  "Given a sequence of text strings, with nils optionally interspersed,
  joins the non-nil elements and word-wraps them to fit the current
  number of *columns*, returning the wrapped string."
  [& ss]
  (str/join "\n" (wrap-line *columns* (join-text " " ss))))
  