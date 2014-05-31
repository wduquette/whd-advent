;;;; whd-advent.core -- Main File for Will's Text Adventure
;;;;
;;;; This is a classic text adventure that I'm working on as an exercise
;;;; in learning Clojure.

(ns whd-advent.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:use whd-advent.rooms))

;;; Game Data
;;;
;;; This is the stuff that changes.

;;; Command Translator
;;;
;;; The Command Translator translates the user's command into a function to
;;; be called, and returns the function.  The returned function will often
;;; mutate memory, e.g., it may move the player from one room to another,
;;; change the location of things, and so on.  When called, the returned 
;;; function should return a text message to be displayed to the user
;;; (possibly "").
;;;
;;; The command translator is implemented as a multi-method.
;;;
;;; TBD: This will eventually go in its own namespace.

(defmulti xlate-command 
  "Translate the user's command into a function.  The command is received
  as a sequence of words."
  (fn [words] (first words)))

(defmethod xlate-command :default
  [words]
  (fn [] "I'm sorry, I don't understand."))

(defmethod xlate-command "help"
  [words]
  (fn [] "I'm sorry, there's no help for you."))

(defmethod xlate-command "quit"
  [words]
  #(System/exit 0))

(defn eval-command [s]
  (let [words (str/split s #"\s"),
        f (xlate-command words)]
    (f)))


;;; Main Routine

(defn -main
  "Main routine for the application."
  [& args]
  (println "Will's Text Adventure")
  (println)
  (loop []
    (println)
    (println "Nowhere to be") ; TBD: Should be the local description
    (print "\nWell? ")
    (flush)
    (println (eval-command (read-line)))
    (recur)))
