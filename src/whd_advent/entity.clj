;;;; entity.clj
;;;;
;;;; Entity definition and query code for Will's Text Adventure.
;;;; A "entity" is some kind of object in the game: a room, a thing the
;;;; can pick up or interact with.
;;;;
;;;; In this module, the argument "e" means a entity ID keyword.

(ns whd-advent.entity
  (:require [clojure.string :as str])
  (:use whd-advent.debug)
  (:use whd-advent.tools)
  (:use whd-advent.describe))


;;; # Defining Entities
;;;
;;; A entity is identified by a unique keyword and a type, and consists of a map
;;; with the following keys:
;;;
;;;  * `:type` - The entity's type, e.g., `:room`.
;;;  * `:name` - The entity's short name, used for display.
;;;  * `:description` - The entity's full description, which can take a
;;;     variety of forms.
;;;  * Any arbitrary keywords and values needed as the game is developed.
;;;
;;; All entity definitions are stored in an atom called `entities`, which 
;;; contains a map from entity keywords to entity structures.
;;; Use `(define-entity ...)` to add a new entity.  NOTE: most entity types
;;; will define their own `(define-<type>)` function.
;;;
;;; Note that although entities is an atom, entity definitions are intended to
;;; be immutable once game-play has begun.  Game state is stored elsewhere.

(def entities (atom {}))

(defn- make-entity
  "Returns a entity structure, given the entity's type, name and description,
  and any optional data values as a map in `extra-info`"
  [type name description extra-info]
  (merge {:type         type
          :name         name 
          :description  description} 
          extra-info))

(defn define-entity 
  "Creates a entity structure, given the entity's ID, type, name, and 
  description, and any optional data values, and adds it to the collection of 
  entities."
  [e & entity-info]
  (swap! entities assoc e (apply make-entity entity-info))
  e)

  
;;; ## Entity Queries
;;;
;;; These functions are used to query entities..

(defn =>
  "Access entities maps and entity attributes.  Given a entity, returns the entity's
  map.  Given a entity and one or more keys, drills down into nested maps."
  ([e] (@entities e))
  ([e & rest] (get-in (@entities e) rest)))

(defn entity?
  "Returns true if the given value is an entity keyword, and false
   otherwise."
  [e]
  (contains? @entities e))

(defn describe-entity
  "Returns a text description of a entity.
  TODO: merge describe.clj into this module.  Then this will simply be
  called `describe`."
  [e]
  (describe (@entities e)))

