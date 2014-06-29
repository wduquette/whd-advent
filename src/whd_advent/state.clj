;;;; state.clj -- Mutable game state
;;;;
;;;; This module defines all mutable game state: everything that can change
;;;; once the game begins.  In other words, to save the game save the
;;;; following atoms:
;;;;
;;;; * player
;;;; * inventory

(ns whd-advent.state
  (:use whd-advent.tools)
  (:use whd-advent.entity)
  (:use whd-advent.room)
  (:use whd-advent.thing))

;;; # The Player Map
;;;
;;; The player structure is a map that accumulates information about the
;;; the player: his current location (a room) and such-like:
;;;
;;; * `:here` - The room keyword for the player's current location.
;;;
;;; * `:contents` - The player's initial inventory.
;;;   TODO: Define an entity or data-structure in world.clj that gives the
;;;   player's initial data; then, we'll initialize it on game start.
;;;
;;; * `:state` - :playing or :done.  This is a sentinel used to end the
;;;   game loop.

(def player
  (atom
    {:state    :playing
     :here     :home
     :contents #{}}))

;;; Player Queries

(defn here
  "Returns the current location of the player."
  []
  (@player :here))

;;; Player Mutators
;;;
;;; These functions modify the @player map.

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


;;; The Inventory Map
;;;
;;; The inventory map is a map from inventory ID to inventories.
;;; Inventory IDs include the :player, room IDs, and thing IDs for 
;;; those things that can have content.  There is also a :limbo inventory, 
;;; for objects with nowhere else to be.  An inventory represents the
;;; current content of the given entity, and is represented as a set
;;; of thing IDs.

(def inventory (atom {}))

;;; Inventory Queries

(defn container-of
  "Returns the inventory containing thing t, or nil if none."
  [t]
  (first
    (for [k (keys @inventory) :when (contains? (@inventory k) t)] k)))

(defn has?
  [i t]
  (contains? (@inventory i) t))

;;; Inventory Mutators
;;;
;;; These functions mutate the inventory map.

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



;;; Inventory Output
;;;
;;; These commands display inventories to the user.
  
(defn describe-inventory
  [i s]
  ;; FIXME: should be (say), but (say) needs
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
