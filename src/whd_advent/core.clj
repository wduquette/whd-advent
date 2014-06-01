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

(def player 
  (atom 
    {;; Where the player is located
     :here :home}))

;;; Game Data Queries

(defn here
  []
  (@player :here))


;;; Actions
;;;
;;; The user enters commands; a command is translated into an action, which
;;; is then evaluated in the game-repl.  Actions can mutate the game state, 
;;; though they need not.
;;;
;;; The following functions can be used to build actions.

(defn say 
  "Output one or more items to the user.  TBD: Ultimately, I'll want to
  provide some more processing here, i.e., word-wrapping."
  [& xs]
  (apply println xs))

(defn move-to!
  "Magic move the player to the given room (which must exist)."
  [room]
  {:pre [(is-room? room)]} ; It's a valid room: it has an entry in rooms.
  (swap! player assoc :here room))

(defn move!
  "Try to move the player in a direction."
  [dir]
  {:pre [(is-dir? dir)]}
  (let [room (next-room (here) dir)]
     (if room 
       (move-to! room)
       (say "You can't go that way."))))

(defn quit-game!
  "Quit the game.  TBD: Should prompt to save, etc."
  []
  (say "Your loss, toots!")
  (System/exit 1))

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
  #(say "I'm sorry, I don't understand."))

(defmethod xlate-command "help"
  [words]
  #(say "I'm sorry, there's no help for you."))

(defmethod xlate-command "quit"
  [words]
  #(quit-game!))

(defn eval-command [s]
  (let [words (str/split s #"\s"),
        f (xlate-command words)]
    (f)))


;;; Main Routine

(defn prompt [text]
  (printf "%s " text)
  (flush))

(defn -main
  "Main routine for the application."
  [& args]
  (println "Will's Text Adventure")
  (loop []
    (println)
    (say (describe-room (here)))
    (prompt "Well?")
    (eval-command (read-line))
    (recur)))
