;;;; thing.clj
;;;;
;;;; Thing definition and query code for Will's Text Adventure.
;;;; A "thing" is an object in the game, but the word "object" is overloaded.
;;;;
;;;; In this module, the argument "thing" means a thing ID keyword, and 
;;;; "t" means a thing's data map.

(ns whd-advent.thing
  (:require [clojure.string :as str])
  (:use whd-advent.debug)
  (:use whd-advent.tools)
  (:use whd-advent.describe))


;;; # Defining Things
;;;
;;; A thing is identified by a unique keyword, and consists of a map
;;; with the following keys:
;;;
;;;  * `:name` - The thing's short name
;;;  * `:description` - The thing's full description.
;;;  * Any arbitrary keywords and values needed as the game is developed.
;;;
;;; All thing definitions are stored in an atom called `things`, which 
;;; contains a map from thing keywords to thing structures.
;;; Use `(define-thing ...)` to add a new thing.
;;;
;;; Note that although things is an atom, thing definitions are intended to
;;; be immutable once game-play has begun.  Game state is stored elsewhere.

(def things (atom {}))

(defn- make-thing
  "Returns a thing structure, given the thing's name and description,
  and any optional data values."
  [name description & extra-info]
  (merge {:name         name 
          :description  description} 
         (apply hash-map extra-info)))

(defn define-thing 
  "Creates a thing structure, given the thing's ID, name, and description,
  and any optional data values, and adds it to the collection of things."
  [kw & thing-info]
  (swap! things assoc kw (apply make-thing thing-info))
  kw)

  
;;; ## Thing Queries
;;;
;;; These functions are used to query things..

(defn thing
  "Access things maps and thing attributes.  Given a thing, returns the thing's
  map.  Given a thing and one or more keys, drills down into nested maps."
  ([thing] (@things thing))
  ([thing & rest] (get-in (@things thing) rest)))

(defn is-thing?
  "Returns true if the given value is a thing keyword, and false
   otherwise."
  [thing]
  (contains? @things thing))

(defn describe-thing
  "Returns a text description of a thing."
  [thing]
  (describe (@things thing)))

