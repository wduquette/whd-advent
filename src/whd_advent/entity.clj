;;;; entity.clj
;;;;
;;;; Entity definition and query code for Will's Text Adventure.
;;;; A "entity" is some kind of object in the game: a room, a thing the
;;;; can pick up or interact with.
;;;;
;;;; In this module, the argument "e" means a entity ID keyword.

(ns whd-advent.entity
  (:require [clojure.string :as str])
  (:use whd-advent.tools)
  (:use whd-advent.facts))


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

(defn define-entity 
  "Creates a entity structure, given the entity's type, ID, type, name, and 
  description, and any optional data values, and adds it to the collection of 
  entities."
  [type e name description & rest]
  (let [base [:type type :name name :description description]]
    (swap! entities assoc e (apply hash-map (concat base rest)))
  e))


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

;;; #E Entity Descriptions

(defmulti describe
  "Determine how the description is to be computed.  At present,
  the entity `e`'s `:description` can be:

  1. A simple string

  2. A sequence of items, each of which is a simple string or a
     [fact, string] pair.  The pair's string is included if the
     fact is true.

  Either way, the result is wrapped to *columns* for output."
  (fn [e]
    (let [d (=> e :description)]
      (cond
        (string? d)     :string
        (sequential? d) :fact-based
        :else
          (throw (ex-info "Indescribable entity" {:entity e}))))))

;; The description is simply a string.
(defmethod describe :string
  [e]
  (wrap-text (=> e :description)))

;; The description is a sequence of strings with embedded facts.
(defmethod describe :fact-based
  [e]
  (wrap-text
    (map (fn [x]
           (if (string? x) x         ; The element is a string, or
             (let [[f t] x]          ; a [fact, string] pair.
               (if (fact? f) t))))   ; Include the string if the fact
       (=> e :description))))        ; is true.

