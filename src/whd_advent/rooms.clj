;;;; rooms.clj
;;;;
;;;; Room definitions for Will's Text Adventure

(ns whd-advent.rooms
  (:require [clojure.string :as str])
  (:use whd-advent.tools))

;;; ## Directions
;;;
;;; Directions are stored internally as keywords.  The values
;;; in the dir-names map are for display, rather than for input; see 
;;; `whd-advent.vocab` for the command vocabulary. 

(def dir-names {
  :n "North",
  :s "South",
  :e "East",
  :w "West"})


;;; ## Defining Rooms
;;;
;;; A room is identified by a unique keyword, and consists of a map
;;; with the following keys:
;;;
;;;  * `:title` - The room's short name
;;;  * `:links` - A map whose keys are direction keywords and values are room
;;;    keywords
;;;  * `:description` - The room's full description.
;;;  * Any arbitrary keywords and values needed as the game is developed.
;;;
;;; Taken altogether, the rooms form the world map, which is stored in an atom 
;;; called `rooms`, and consists of a map from room keywords to room structures.
;;; Use `(define-room ...)` to add a room to the map.

(def rooms (atom {}))

(defn- make-room
  "Returns a room structure, given the room's title, links, and description,
  and any optional data values.  "
  [title links description & extra-info]
  (merge {:title title :links links :description (wrap-text description)}
         (apply hash-map extra-info)))

(defn define-room 
  "Creates a room structure, given the room's ID, title, links, and description,
  and any optional data values, and adds it to the world map."
  [kw & room-info]
  (swap! rooms assoc kw (apply make-room room-info)))

  


;;; ## World Map
;;;
;;; This is the world map data
(define-room :home "Home Base" 
  {:n :street}
  "Your home base is the picture of comfort.")

(define-room :street "The Street" 
  {:s :home :e :corner :w :park}
  "It's gritty here in the mean streets.")

(define-room :corner "The Corner"
  {:w :street}
  "You're down on the corner.")

(define-room :park "Neighborhood Park"
  {:e :street}
  "You're in the park.")

;;; ## Room Queries
;;;
;;; These functions are used to query the world map and individual rooms.

(defn is-room?
  "Returns true if the given value is a room keyword, and false
   otherwise."
  [room]
  (if (@rooms room) true false))

(defn is-dir?
  "Returns true if the given value is a direction keyword, and false
  otherwise."
  [dir]
  (if (dir-names dir) true false))

(defn next-room 
  "Given a room and a direction, get the next room in that direction or 
  nil if none.  Ultimately we can put in logic to allow computed 
  destinations or blocked links."
  [room dir]
  (-> @rooms room :links dir))

(defn room-title 
  "Returns the room's title."
  [room]
  (-> @rooms room :title))

(defn describe-room
  "Returns a text description of a room, including the directions you can
  go.  Ultimately, this should probably take some options, indicating which
  information you want to include or not include in the return value."
  [room]
  (let [{:keys [title links description]} (@rooms room)]
    (format "%s\n%s\n\nYou can go: %s\n" title description
            (str/join ", " (map dir-names (keys links))))))
