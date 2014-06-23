;;;; room.clj
;;;;
;;;; Room definition and query code for Will's Text Adventure.
;;;;
;;;; In this module, the argument "r" means a room ID keyword.

(ns whd-advent.room
  (:require [clojure.string :as str])
  (:use whd-advent.debug)
  (:use whd-advent.tools)
  (:use whd-advent.entity))

;;; # Directions
;;;
;;; Directions are stored internally as keywords.  The values
;;; in the dir-names map are for display, rather than for input; see 
;;; `whd-advent.vocab` for the command vocabulary. 

(def dir-names {
  :n "North",
  :s "South",
  :e "East",
  :w "West"})


;;; # Defining Rooms
;;;
;;; A room is an entity (see entity.clj).  It has the standard attributes,
;;; plus these:
;;;
;;;  * `:links` - A map whose keys are direction keywords and values are room
;;;    keywords
;;;  * Any arbitrary keywords and values needed as the game is developed.
;;;
;;; Taken altogether, the rooms form the world map.
;;; Use `(define-room ...)` to add a room to the map.

(defn define-room 
  "Creates a room structure, given the room's ID, title, links, and description,
  and any optional data values passed as a map in `extra-info`, and adds it to 
  the world map."
  [r name links description & rest]
  (let [base [:room r name description :links links]]
    (apply define-entity (concat base rest)))) 

;;; ## Room Queries
;;;
;;; These functions are used to query the world map and individual rooms.

(defn room?
  "Returns true if the given value is a room keyword, and false
   otherwise."
  [r]
  (= (=> r :type) :room))

(defn dir?
  "Returns true if the given value is a direction keyword, and false
  otherwise."
  [dir]
  (contains? dir-names dir))

(defn next-room 
  "Given a room and a direction, get the next room in that direction or 
  nil if none.  Ultimately we can put in logic to allow computed 
  destinations or blocked links."
  [r dir]
  (=>  r :links dir))

(defn describe-room
  "Returns a text description of a room, including the name.
  TODO: This concatenation should be done elsewhere."
  [r]
  (format "%s\n%s" (=> r :name) (describe r)))

(defn describe-exits
  "Returns a description of the directions you can go from the `room`."
  [r]
  (let [links (=> r :links)
        text (str/join ", " (map dir-names (keys links)))] 
    (str "You can go: " text )))
