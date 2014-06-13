;;;; rooms.clj
;;;;
;;;; Room definitions for Will's Text Adventure

(ns whd-advent.rooms
  (:require [clojure.string :as str]))

;;; ## Directions
;;;
;;; Directions are stored internally as keywords.  The values
;;; in the dir-names map are for display, rather than for input.

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

(defn- make-room
  "Returns a room structure, given the room's title, links, and description,
  and any optional data values.  "
  [title links description & extra-info]
  (merge {:title title :links links :description description}
         (apply hash-map extra-info)))


;;; ## The Game Map
;;;
;;; The game map is stored as a map from room keywords to room structures
;;; in the "rooms" variable.  There are a couple of other things I could do.
;;;
;;; 1. The rooms var could contain an atom; then, a (define-room) function
;;;    could define a complete room and associate it with the atom.  This
;;;    would be appropriate if I were implementing an adventure game
;;;    framework into which the game data was loaded.
;;;
;;; 2. Rooms could be defined in separate modules (i.e., "areas"), and then
;;;    all rooms could be pulled together by passing the area vars to one
;;;    function that initialized the rooms var.  This function could possibly
;;;    do some computation or sanity checking or caching at the same time,
;;;    all without using mutable variables.

(def rooms {
  :home
    (make-room "Home Base" {:n :street}
               "Your home base is the picture of comfort.")
  :street
    (make-room "The Street" {:s :home :e :corner :w :park}
               "It's gritty here in the mean streets.")
  :corner
    (make-room "The Corner" {:w :street}
               "You're down on the corner.")
  :park
    (make-room "Neighborhood Park" {:e :street}
               "You're in the park.")})

;;; ## Room Queries
;;;
;;; These functions are used to query the world map and individual rooms.

(defn is-room?
  "Returns true if the given value is a room keyword, and false
   otherwise."
  [room]
  (if (rooms room) true false))

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
  (-> rooms room :links dir))

(defn room-title 
  "Returns the room's title."
  [room]
  (-> rooms room :title))

(defn describe-room
  "Returns a text description of a room, including the directions you can
  go.  Ultimately, this should probably take some options, indicating which
  information you want to include or not include in the return value."
  [room]
  (let [{:keys [title links description]} (rooms room)]
    (format "%s\n%s\n\nYou can go: %s\n" title description
            (str/join ", " (map dir-names (keys links))))))
