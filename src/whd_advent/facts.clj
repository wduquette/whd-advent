;;;; facts.clj
(ns whd-advent.facts
    (:use whd-advent.debug))

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
;;;; Facts are added to the Fact Base using the `add-fact!` action, and
;;;; can be deleted using `remove-fact!`.
;;;;
;;;; The Fact Base is a set stored in the `facts` atom.

(def facts 
   ;; These are the initial facts; they represent problems for the user to
   ;; solve, or conditions for the user to change.
   ;; TBD: If this were a separable framework, the game would need to add
   ;; its own facts at start-up.

  (atom #{
   [:sewer :clogged]
   [:tv :broken]
}))

(defn add-fact! 
  "Add fact `f` to the set of known facts."
  [f]
  (swap! facts conj f))

(defn remove-fact! 
  "Remove fact `f` from the set of known facts, if in fact it is there."
  [f]
  (swap! facts disj f))

(defn fact?
  "Returns true if fact `f` (a fact vector) is contained in the fact base,
  and false otherwise.  If the first token of `f` is `:not`, the sense
  is inverted."
  [f]
  (if (not= (first f) :not)
    (contains? @facts f)
    (not (contains? @facts (rest f)))))

(defn not-fact?
  "Returns true if fact `f` (a fact vector) is NOT contained in the fact
  base, and false otherwise.  If the first token of `f` is `:not`, the sense
  is inverted."
  [f]
  (not (fact? f)))

(defn if-fact
  "Returns x if f is a fact, and y (or nil) otherwise.  f is interpreted
  as for (fact?)."
  ([f x]   (if-fact f x nil))
  ([f x y] (if (fact? f) x y)))
