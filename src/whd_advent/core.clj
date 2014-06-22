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
  (:use whd-advent.describe)
  (:use whd-advent.facts)
  (:use whd-advent.room)
  (:use whd-advent.vocab)
  (:use whd-advent.rooms))

;;; ## Game Data
;;;
;;; The game data currently consists of two things: the player structure and
;;; the fact base.

;;; # The Player Structure
;;;
;;; The player structure is a map that accumulates information about the
;;; the player: his current location (a room) and such-like:
;;;
;;; * `:here` - The room keyword for the player's current location.

;;; The `player` variable contains the player structure.

(def player
  (atom
    {:state :playing
     :here  :home}))

(defn here
  "Returns the current location of the player."
  []
  (@player :here))


;;; ## Actions
;;;
;;; Actions are functions executed in response to user commands.  Most
;;; actions update the game state; i.e., they change the location, update
;;; the fact base, and so on.
;;;
;;; Commands are translated into actions by the command translater; and
;;; the resulting actions are executed by the game REPL.  Note that
;;; the action functions are often anonymous.
;;;
;;; The following functions can be used to build actions.

(defn say
  "Output one or more items to the user.
  TBD: Ideally, if there are consecutive items it should put a space
  between them."
  [& xs]
  (doseq [s xs]
    (case s
      :br (print "\n")
      :para (print "\n\n")
      (print s)))
  (print "\n"))

(defn set-state!
  "Sets a new game state.  Normally it's :playing or :done."
  [kw]
  (swap! player assoc :state kw))

(defn move-to!
  "Magic move the player to the given `room` (which must exist)."
  [room]
  {:pre [(is-room? room)]} ; It's a valid room: it has an entry in rooms.
  (swap! player assoc :here room))

(defn move!
  "Try to move the player in direction `dir`, notifying the user if this
  is not possible."
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
  (set-state! :done))

;;; ## Command Translator
;;;
;;; The Command Translator translates the user's command into a function to
;;; be called, and returns the function.  The returned function is either
;;; an action, as defined above, or an anonymous function defined in terms
;;; of one or more actions.
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
  (printf "[%s] %s " (room-title (here)) text)
  (flush))

(defn describe-surroundings
  "Describe the `room` to the player if it hasn't been seen before.
  Then, remember that it has been seen."
  [room]
  (when (fact? [:not :has-seen room])
    (add-fact! [:has-seen room])
    (say (describe-room room) :para
         (describe-exits room))))

(defn -main
  "Main routine for the application, including the game REPL."
  [& args]
  (set-state! :playing)
  (println "Will's Text Adventure")
  (loop []
    (println)
    (describe-surroundings (here))
    (prompt "Well?")
    (eval-command (read-line))
    (when (= (@player :state) :playing)
      (recur))))
