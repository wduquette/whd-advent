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

;;;; TBD: it might be possible to pretty this up even further.  If
;;;; there's one token, a sequence, it's the fact vector.  Otherwise,
;;;; take all of the arguments as one fact vector.

;;;; The Fact Base is a set stored in the `facts` atom.

(def facts (atom #{}))

(defn set-fact! 
  "Add fact `f` to the set of known facts."
  [f]
  (swap! facts conj f))
  
(defn fact?
  "Returns true if fact `f` (a fact vector) is contained in the fact base,
  and false otherwise.  If the first token of `f` is `:not`, the sense
  is inverted."
  [f]
  (if (not= (first f) :not)
    (contains? @facts f)
    (not (contains? @facts (rest f)))))

(defn not-fact?
  "Retruns true if fact `f` (a fact vector) is NOT contained in the fact
  base, and false otherwise.  If the first token of `f` is `:not`, the sense
  is inverted."
  [f]
  (not (fact? f)))
