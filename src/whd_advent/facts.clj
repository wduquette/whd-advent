;;;; facts.clj
(ns whd-advent.facts)

;;;;
;;;; # The Fact Base
;;;;
;;;; The Fact Base is a set of facts that are true about the game world: 
;;;; what rooms have been visited, what conditions have been met, and like
;;;; that.  The notion is that during game play, facts will be added to the
;;;; Fact Base; and the defined facts will control how the game plays out.
;;;;
;;;; Each fact is a vector of two or three items.  The current set of facts
;;;; are as follows:
;;;;
;;;; * `[has-seen room]` -- The player has seen the given `room`.  This
;;;;   determines whether the room's full description is given or not.
;;;;
;;;; Facts are added to the Fact Base using the `set-fact!` action.


;;;; The Fact Base is a set stored in the `facts` atom.  It is empty
;;;; at the beginning of the game.

(def facts (atom #{}))

(defn set-fact! 
  "Add fact `f` to the set of known facts."
  [f]
  (swap! facts conj f))
  
(defn is-fact?
  "Returns true if fact `f` (a fact vector) is contained in the fact base,
  and false otherwise."
  [f]
  (if (@facts f) true false))

(defn is-not-fact?
  "Retruns true if fact `f` (a fact vector) is NOT contained in the fact
  base, and false otherwise."
  [f]
  (not (is-fact? f)))
