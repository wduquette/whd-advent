;;;; parser.clj
;;;;
;;;; Code for defining words and sentences, and parsing them.

(ns whd-advent.vocab
  (:require [clojure.string :as str])
  (:use whd-advent.tools))





(comment
  ;; Some functions I want to define.
  (define-word :direction   :n        "n" "north")
  (define-word :noun        :ball     "ball")
  (define-word :noun        :couch    "couch" "sofa")
  (define-word :verb        :get      "get" "take")
  (define-word :verb        :drop     "drop")
  (define-word :verb        :go       "go")
  (define-word :adjective   :red      "red")
  (define-word :adjective   :battered "battered")
  (define-word :preposition :to       "to") 
  
  ;;; I want to specify a command as a pattern to be matched.  The
  ;;; pattern needs to include constants and typed variables.  And
  ;;; I need to avoid confusion between constants and types.
  ;;;
  ;;; I'm doing something like destructuring here; I might want to
  ;;; use a macro, ultimately.
  ;;;
  ;;; The types I absolutely need are:
  ;;;
  ;;; * :verb - A verb
  ;;; * :direction - A direction word
  ;;; * :noun-phrase - I.e., a noun, an adjective-noun pair, 
  ;;;   or an adjective standing in for a noun.
  ;;; * :prep-phrase - I.e., a preposition and a noun-phrase.
  
  (define-command [:direction d]]
    (move! d))
  (define-command [:verb :go :direction d]]
    (move! d))
  
  (define-command [:verb :get :noun-phrase [a n]]
    ;; Determine t from [a n]
    ;; If it's not present, say so; otherwise,
    (place-thing! t :player))

  ;;; define-command is getting the levels mixed.  I really want to define
  ;;; commands at the level of (:get <thing t>).
  ;;; And that means that I want a (define-pattern) command that purely
  ;;; defines a pattern; and then we can match it to commands and things.
)
