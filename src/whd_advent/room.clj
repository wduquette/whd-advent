;;;; room.clj
;;;;
;;;; Room definition and query code for Will's Text Adventure.
;;;;
;;;; In this module, the argument "room" means a room ID keyword, and 
;;;; "r" means a room's data map.

(ns whd-advent.room
  (:require [clojure.string :as str])
  (:use whd-advent.tools)
  (:use whd-advent.facts))

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

;;; # Room Description Hooks
;;;
;;; A room description hook is a function that returns a room's current
;;; description.  The default hook simply returns the room's :description.

(defn default-room-description-hook
  "Wraps and returns the room's :description as is, given room map `r`."
  [r]
  (wrap-text (r :description)))

(defn fact-based-room-description-hook
  "Wraps returns the room's description, treating the :description as a
   sequence of items to be included in the description.  An item can be
   one of the following:

   1. A simple text string, to be included as is
   2. A fact/text string pair, to be included if the fact is true, per `fact?`."
  [r]
  (wrap-text 
    (map (fn [x]
           (if (string? x) x
             (let [[f t] x]
               (if (or (true? f) (fact? f)) t))))
         (r :description))))


;;; # Defining Rooms
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
  and any optional data values:

  * `:description-hook` - A function that takes the room's map and returns a
    complete description."
  [title links description & extra-info]
  (merge {:title            title 
          :links            links 
          :description      description 
          :description-hook default-room-description-hook}
         (apply hash-map extra-info)))

(defn define-room 
  "Creates a room structure, given the room's ID, title, links, and description,
  and any optional data values, and adds it to the world map."
  [kw & room-info]
  (swap! rooms assoc kw (apply make-room room-info))
  kw)

  
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
  "Returns a text description of a room, including the title"
  [room]
  (let [r (@rooms room) 
        {:keys [title description-hook]} r]
    (format "%s\n%s" title (description-hook r))))

(defn describe-exits
  "Returns a description of the directions you can go from the `room`."
  [room]
  (let [links (-> @rooms room :links)
        text (str/join ", " (map dir-names (keys links)))] 
    (str "You can go: " text )))
