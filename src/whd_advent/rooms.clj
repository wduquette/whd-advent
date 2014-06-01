;;;; rooms.clj
;;;;
;;;; Room definitions for Will's Text Adventure

(ns whd-advent.rooms
  (:require [clojure.string :as str]))

;;; FIRST, some functions for defining rooms.

(defn- make-room
  "Returns a room structure"
  [title links description & extra-info]
  (merge {:title title :links links :description description}
         (apply hash-map extra-info)))

;;; NEXT, links are to directions.

(def dir-names {
  :n "North",
  :s "South",
  :e "East",
  :w "West"})

;;; NEXT, the room definitions.
;;; TBD: One could build rooms up out of multiple sub-maps, possibly
;;; doing some computation or checking at the same time.

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

;;; NEXT, some functions for querying rooms.

(defn is-room?
  [room]
  (if (rooms room) true false))

(defn is-dir?
  [dir]
  (if (dir-names dir) true false))

(defn next-room 
  "Get the next room in the given direction, or nil.
  TBD: Ultimately, we can fancy this up with computed links."
  [room dir]
  (-> rooms room :links dir))

(defn describe-room
  "Returns a text description of a room."
  [room]
  (let [{:keys [title links description]} (rooms room)]
    (format "%s\n%s\n\nYou can go: %s\n" title description
            (str/join ", " (map dir-names (keys links))))))
