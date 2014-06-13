;;;; whd-advent.core -- Main File for Will's Text Adventure
;;;;
;;;; This is a classic text adventure that I'm working on as an exercise
;;;; in learning Clojure.  This namespace contains the main routine, plus
;;;; code that's still being prototyped and will be moved into other namespaces
;;;; eventually.

(ns whd-advent.core
  (:gen-class)
  (:require [clojure.string :as str])
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
    {:here :home}))

(defn here
  "Returns the current location of the player."
  []
  (@player :here))


;;; # The Fact Base
;;;
;;; The Fact Base is a set of facts that are true about the game world: 
;;; what rooms have been visited, what conditions have been met, and like
;;; that.  The notion is that during game play, facts will be added to the
;;; Fact Base; and the defined facts will control how the game plays out.
;;;
;;; Each fact is a vector of two or three items.  The current set of facts
;;; are as follows:
;;;
;;; * `[has-seen room]` -- The player has seen the given `room`.  This
;;;   determines whether the room's full description is given or not.
;;;
;;; Facts are added to the Fact Base using the `set-fact!` action.


;;; The Fact Base is a set stored in the `facts` atom.  It is empty
;;; at the beginning of the game.

(def facts (atom #{}))
  
(defn is-fact? [f]
  "Returns true if fact `f` (a fact vector) is contained in the fact base,
  and false otherwise."
  (if (@facts f) true false))

(defn is-not-fact? [f]
  "Retruns true if fact `f` (a fact vector) is NOT contained in the fact
  base, and false otherwise."
  (not (is-fact? f)))

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
  "Output one or more items to the user.  TBD: Ultimately, I'll want to
  provide some more processing here, i.e., word-wrapping."
  [& xs]
  (apply println xs))

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

(defn set-fact! 
  "Add fact `f` to the set of known facts."
  [f]
  (swap! facts conj f))

(defn quit-game!
  "Quit the game.  TBD: Should prompt to save, etc."
  []
  (say "Your loss, toots!")
  (System/exit 1))

;;; # Command Translator
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
  ;; TBD: handle excess words
  #(quit-game!))

(defmethod xlate-command "look"
  [words]
  ;; TBD: handle excess words
  #(say (describe-room (here))))

;;; TBD: It would be nice to have a look-up from command words to keywords.
(defmethod xlate-command "n"
  [words]
  ;; TBD: handle excess words
  #(move! :n))

(defmethod xlate-command "s"
  [words]
  ;; TBD: handle excess words
  #(move! :s))

(defmethod xlate-command "e"
  [words]
  ;; TBD: handle excess words
  #(move! :e))

(defmethod xlate-command "w"
  [words]
  ;; TBD: handle excess words
  #(move! :w))


(defn eval-command [s]
  (let [words (str/split s #"\s"),
        f (xlate-command words)]
    (f)))


;;; # Main Routine

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
  (when (is-not-fact? [:seen room])
    (say (describe-room room))
    (set-fact! [:seen room])))

(defn -main
  "Main routine for the application, including the game REPL."
  [& args]
  (println "Will's Text Adventure")
  (loop []
    (println)
    (describe-surroundings (here))
    (prompt "Well?")
    (eval-command (read-line))
    (recur)))
