;;;; vocab.clj
;;;;
;;;; Vocabulary for Will's Text Adventure
(ns whd-advent.vocab
  (:require [clojure.string :as str])
  (:use whd-advent.tools))

;;; ## Vocabulary Definition
;;;
;;; The game's vocabulary consists of words of different parts of speech.
;;; They are represented in code as keywords.  This module contains the
;;; mapping from English words to keywords.
;;;
;;; Each part of speech has its own map, which is a map from English words
;;; to keywords.  No English word can map to more than one keyword, but
;;; synonyms will map to the same keyword.

;;; # Verb Definition
;;;
;;; Verbs are words that map ultimately to actions.  The `verbs` map 
;;; contains the mapping, which is created using `define-verb`.

(def verbs (atom {}))

(defn define-verb
  "Defines a new verb with the keyword `kw`, and specifies one or more
  English equivalents.  If the verb is already known, any new synonyms
  are added to the `verbs` map."
  [kw & synonyms]
  (doseq [s synonyms]
    (swap! verbs assoc s kw)))
  
(defn verb
  "Given an English word, returns the matching verb keyword, or nil if none."
  [w]
  (@verbs (str/lower-case w)))

;;; ## Basic Vocabulary
;;;
;;; Basic vocabulary is defined directly in this namespace.  Other modules
;;; can use the `define-*` functions to define words as well.

;;; # Basic Verbs

(define-verb :help "?" "help")
(define-verb :look "l" "look")
(define-verb :exits "exits")
(define-verb :quit "quit" "bye")
(define-verb :inventory "i" "invent" "inventory")

(define-verb :n "n" "north")
(define-verb :s "s" "south")
(define-verb :e "e" "east")
(define-verb :w "w" "west")
