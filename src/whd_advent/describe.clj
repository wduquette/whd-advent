;;;; describe.clj
;;;;
;;;; `(describe)` is a multi-method for computing descriptions of
;;;; entities, where an entity is a room, a thing, a mob, or whatever.
;;;; To be describable, the entity must:
;;;;
;;;; * Be represented as a map.
;;;; * Have a `:description` element.
;;;; * The `:description` element must have a form understood by
;;;;   one of `(describe)`'s methods.

(ns whd-advent.describe
  (:require [clojure.string :as str])
  (:use whd-advent.debug)
  (:use whd-advent.tools)
  (:use whd-advent.facts))

(defmulti describe
  "Determine how the description is to be computed.  At present,
  the entity `e`'s `:description` can be:

  1. A simple string

  2. A sequence of items, each of which is a simple string or a
     [fact, string] pair.  The pair's string is included if the
     fact is true.

  Either way, the result is wrapped to *columns* for output."
  (fn [e]
    (let [d (e :description)]
      (cond
        (string? d)     :string
        (sequential? d) :fact-based
        :else
          (throw (ex-info "Indescribable entity" {:entity e}))))))

;; The description is simply a string.
(defmethod describe :string
  [e]
  (wrap-text (e :description)))

;; The description is a sequence of strings with embedded facts.
(defmethod describe :fact-based
  [e]
  (wrap-text
    (map (fn [x]
           (if (string? x) x         ; The element is a string, or
             (let [[f t] x]          ; a [fact, string] pair.
               (if (fact? f) t))))   ; Include the string if the fact
       (e :description))))           ; is true.
