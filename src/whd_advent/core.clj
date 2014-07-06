;;;; whd-advent.core -- Main File for Will's Text Adventure
;;;;
;;;; This is a classic text adventure that I'm working on as an exercise
;;;; in learning Clojure.  This namespace contains the main routine, plus
;;;; code that's still being prototyped and will be moved into other namespaces
;;;; eventually.

(ns whd-advent.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:use whd-advent.tools)
  (:use whd-advent.facts)
  (:use whd-advent.vocab)
  (:use whd-advent.entity)
  (:use whd-advent.room)
  (:use whd-advent.thing)
  (:use whd-advent.world)
  (:use whd-advent.state))

;;; ## Command Translator
;;;
;;; The Command Translator translates the user's command into a function to
;;; be called, and returns the function.  The returned function
;;; mutates game state and/or displays information to the user.
;;;
;;; The command translator is implemented as a multi-method; each method
;;; implements one verb.

(defmulti xlate-command
  "Translate the user's command into an action function.  The command is
  received as a sequence of (presumably) English words."
  (fn [words] (verb (first words))))

(defmethod xlate-command :default
  [words]
  #(say "I'm sorry, I don't understand."))

(defmethod xlate-command :help
  [words]
  #(say (wrap-text
          "You can use the usual adventure game commands.
          To quit the game, enter 'quit'.")))

(defmethod xlate-command :quit
  [words]
  ;; TBD: handle excess words
  #(quit-game!))

(defmethod xlate-command :look
  [words]
  ;; TBD: handle excess words
  #(say (describe-room (here)) :para
        (describe-exits (here))))

(defmethod xlate-command :exits
  [words]
  ;; TBD: handle excess words
  #(say (describe-exits (here))))

(defmethod xlate-command :inventory
  [words]
  ;; TBD: handle excess words
  #(describe-player-inventory))

(defmethod xlate-command :n
  [words]
  ;; TBD: handle excess words
  #(move! :n))

(defmethod xlate-command :s
  [words]
  ;; TBD: handle excess words
  #(move! :s))

(defmethod xlate-command :e
  [words]
  ;; TBD: handle excess words
  #(move! :e))

(defmethod xlate-command :w
  [words]
  ;; TBD: handle excess words
  #(move! :w))


(defn eval-command [s]
  (let [words (str/split s #"\s"),
        f (xlate-command words)]
    (f)))


;;; # Main Line Code

(defn prompt
  "Returns the user prompt, which includes the current location and some
  text chosen by the caller."
  [text]
  (printf "[%s] %s " (=> (here) :name) text)
  (flush))

(defn describe-surroundings
  "Describe room `r` to the player."
  [r]
  (say (describe-room r) :para
       (describe-exits r)))

(defn -main
  "Main routine for the application, including the game REPL."
  [& args]
  (init-inventory!)
  (set-state! :playing)
  (println "Will's Text Adventure")
  (loop []
    (println)
    (when (fact? [:not :has-seen (here)])
      (add-fact! [:has-seen (here)])
      (describe-surroundings (here)))
    (prompt "Well?")
    (eval-command (read-line))
    (when (= (@player :state) :playing)
      (recur))))
