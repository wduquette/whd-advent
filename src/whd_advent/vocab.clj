;;;; vocab.clj
;;;;
;;;; Vocabulary for Will's Text Adventure
(ns whd-advent.vocab
  (:require [clojure.string :as str])
  (:use whd-advent.tools))

;;; TODO:
;;; Two purposes:
;;;
;;; 1. Map english words to keywords.
;;; 2. Map keywords to parts of speech.
;;;
;;; Parsing steps:
;;;
;;; 1. Replace words with :keywords.
;;;    1. On unknown word, say "I don't know what $word means."
;;; 2. Remove noise words.
;;; 3. Determine sentence pattern.
;;;
;;; (part-of-speech kw)
;;; (verb? kw)
;;; (noun? kw)
;;; (preposition? kw)


;;; ## Vocabulary Definition
;;;
;;; The game's vocabulary consists of words of different parts of speech.
;;; They are represented in code as keywords.  This module contains the
;;; mapping from English words to logical words (keywords), and the
;;; tools for defining vocabulary.


;;; The words map is a map from logical word keywords to sets, where each
;;; set is a set of parts of speech.  We use a set, because some words can 
;;; be multiple parts of speech. For example, :n is a :direction, but also 
;;; a :verb.
;;;
;;; The valid parts of speech are:
;;;
;;; :verb         - That is, a command.  It tells the game to do something.
;;; :noun         - The name for a thing in the game.
;;; :adjective    - A modifier for a noun.  This allows us to have multiple
;;;                 keys, for example.
;;; :direction    - A direction, e.g., :n, :s.
;;; :preposition  - A preposition, e.g., :to, :from.
;;; :noise        - A noise word, e.g., :the, :a, :an.  Noise words are
;;;                 ignored.

(def words (atom {}))

;;; The synonyms atom is a mapping from English words to keywords.
(def synonyms (atom {}))

(defn- define-synonym
  "Relates one or more English words to a keyword.  If the word is already 
  known, any new synonyms are added to the `synonyms` map."
  [kw ws]
  (doseq [w ws]
    (swap! synonyms assoc (str/lower-case w) kw)))

(defn define-word 
  "Defines a logical word, kw, with one or more parts of speech [ps],
  and one or more synonyms ws."
  [kw ps & ws] 
  (define-synonym kw ws)
  (let [pset (if (keyword? ps) #{ps} (set ps))]
    (swap! words assoc kw pset))
  nil)

(defn- translate-word
  "Given an English word, returns the matching keyword, or nil if none."
  [w]
  (@synonyms (str/lower-case w)))

(defn translate-sentence
  "Given a sentence s, breaks it into words (stripping punctuation) and
  translates the words into keywords.  The result is a 
  vector of word/keyword pairs.  If a word is unknown, the keyword is nil."
  [s]
  (let [ws (re-seq #"\w+" s)]
    (map (fn [w] [w (translate-word w)]) ws)))

(defn has-pos?
  "Returns true if w has the desired part of speech."
  [w pos]
  (contains? (@words w) pos))


;;; # Verb Definition
;;;
;;; Verbs are words that map ultimately to actions.  The `verbs` map 
;;; contains the mapping, which is created using `define-verb`.

(def verbs (atom {}))

(defn define-verb
  "Defines a new verb with the keyword `kw`, and specifies one or more
  English equivalents.  If the verb is already known, any new synonyms
  are added to the `verbs` map."
  [kw & synonyms]
  (doseq [s synonyms]
    (swap! verbs assoc s kw)))
  
(defn verb
  "Given an English word, returns the matching verb keyword, or nil if none."
  [w]
  (@verbs (str/lower-case w)))

;;; ## Basic Vocabulary
;;;
;;; Basic vocabulary is defined directly in this namespace.  Other modules
;;; can use the `define-*` functions to define words as well.

;;; # Basic Verbs

(define-verb :help "?" "help")
(define-verb :look "l" "look")
(define-verb :exits "exits")
(define-verb :quit "quit" "bye")
(define-verb :inventory "i" "invent" "inventory")

(define-verb :n "n" "north")
(define-verb :s "s" "south")
(define-verb :e "e" "east")
(define-verb :w "w" "west")
