;;;; whd-advent.core -- Main File for Will's Text Adventure
;;;;
;;;; This is a classic text adventure that I'm working on as an exercise
;;;; in learning Clojure.  This namespace contains the main routine, plus
;;;; code that's still being prototyped and will be moved into other namespaces
;;;; eventually.

(ns whd-advent.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:use whd-advent.debug)
  (:use whd-advent.tools)
  (:use whd-advent.facts)
  (:use whd-advent.entity)
  (:use whd-advent.room)
  (:use whd-advent.thing)
  (:use whd-advent.vocab)
  (:use whd-advent.world))


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
;;; * `:state` - :playing or :done.  This is a sentinel used to end the
;;;   game loop.

(def player
  (atom
    {:state :playing
     :here  :home
     :contents #{}}))

(defn here
  "Returns the current location of the player."
  []
  (@player :here))


;;; Inventory
;;;
;;; The :player has an inventory, as do those objects with a :contents
;;; attribute.  There is also a :limbo inventory, for objects with nowhere
;;; else to be.  An inventory is a set.

(def inventory (atom {}))

(defn container-of
  "Returns the inventory containing thing t, or nil if none."
  [t]
  (first
    (for [k (keys @inventory) :when (contains? (@inventory k) t)] k)))


(defn place-thing! 
  "Places thing t in inventory i, removing it from its previous container
  (if any)."
  [t i]
  (let [j (container-of t)]
    ;; Remove t from the old inventory, if it's in one.
    (when j (swap! inventory assoc j (disj (@inventory j) t)))
    ;; Next, add it to the new inventory, creating the set if need be.
    (swap! inventory assoc i (into #{t} (@inventory i))))
  nil)

(defn place-things! 
  "Places things ts in inventory i, removing them from their previous containers
  (if any)."
  [ts i]
  (doseq [t ts]
    (place-thing! t i)))

(defn throw-away!
  "Throws thing t away by moving it to :limbo."
  [t]
  (place-thing! t :limbo))

(defn init-inventory!
  "Resets the inventory to its state at the start of the game, stepping
  adding initial :contents from the player, all rooms, and all things."
  []
  (reset! inventory {})
  (place-things! (@player :contents) :player)
  (doseq [e (keys @entities)]
    (place-things! (=> e :contents) e)))
  
(defn describe-inventory
  [i s]
  ;; FIXME: should be (say), but (say) is in the wrong place and needs
  ;; work.
  (println s)
  (doseq [t (@inventory i)]
          (println "  " (=> t :name)))
  (println))

(defn describe-player-inventory 
  "Lists the player's inventory to the console."
  []
  (if (empty? (@inventory :player))
    (println "You're not carrying anything.")
    (describe-inventory :player "You're carrying:")))


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


(defn set-state!
  "Sets a new game state.  Normally it's :playing or :done."
  [kw]
  (swap! player assoc :state kw))

(defn move-to!
  "Magic move the player to the given room `r` (which must exist)."
  [r]
  {:pre [(room? r)]} ; It's a valid room.
  (swap! player assoc :here r))

(defn move!
  "Try to move the player in direction `dir`, notifying the user if this
  is not possible."
  [dir]
  {:pre [(dir? dir)]}
  (let [r (next-room (here) dir)]
     (if r
       (move-to! r)
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
