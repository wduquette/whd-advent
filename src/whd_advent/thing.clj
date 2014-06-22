;;;; thing.clj
;;;;
;;;; Thing definition and query code for Will's Text Adventure.
;;;; A "thing" is an object the player can manipulate or interact with.
;;;;
;;;; In this module, the argument "t" means a thing ID keyword.

(ns whd-advent.thing
  (:require [clojure.string :as str])
  (:use whd-advent.debug)
  (:use whd-advent.tools)
  (:use whd-advent.entity)
  (:use whd-advent.describe))


;;; # Defining Things
;;;
;;; A thing is an entity (see entity.clj).  It has the standard attributes,
;;; plus these:
;;;
;;;  * TBD, but there will be some.
;;;  * Any arbitrary keywords and values needed as the game is developed.
;;;
;;; Use `(define-thing ...)` to add a thing to the game.

(defn define-thing 
  "Defines a thing given the thing's ID, name, and description,
  and any optional data values passed as a map."
  [t name description & rest]
  (let [base [:thing t name description]]
    (apply define-entity (concat base rest)))) 

;;; ## Thing Queries
;;;
;;; These functions are used to query the world map and individual things.

(defn thing?
  "Returns true if the given value is a thing keyword, and false
   otherwise."
  [t]
  (= (=> t :type) :thing))

(defn describe-thing
  "Returns a text description of a thing.
  TODO: this should be the standard (describe) function."
  [t]
  (describe (=> t)))
