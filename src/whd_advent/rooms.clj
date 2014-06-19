;;;; rooms.clj
;;;;
;;;; Actual room definitions for Will's Text Adventure

(ns whd-advent.rooms
  (:require [clojure.string :as str])
  (:use whd-advent.tools)
  (:use whd-advent.facts)
  (:use whd-advent.room))

;;; ## World Map
;;;
;;; This is the world map data

;;; TBD: this is better: a hook that takes the room map and runs with
;;; it, where the additional pieces of text are stored in the map.
;;; Perhaps a standard hook that takes a template [flag kw flag kw]
;;; and builds up the string that way, filtering out nils?
(define-room :home "Home Base" 
  {:n :street}
  "Your home base is the picture of comfort, from the deep 
  pile shag carpeting to the oversized leather couch."
  :tv-broken 
  "Still, you'd be much happier if your big-screen TV was working."
  :template [true :description [:not :tv-works] :tv-broken]
  :description-hook 
  (fn [r] (wrap-text
    (r :description)  
    (if (fact? [:not :tv-works]) (r :tv-broken)))))

(define-room :street "The Street" 
  {:s :home :e :corner :w :park}
  "It's gritty here in the mean streets.  Also smelly, but a bath or two
  might fix that.  Anyway, it's no place to stay for long.")

(define-room :corner "The Corner"
  {:w :street}
  "You're down on the corner.  You think about hanging out for a while,
  but it's no fun without company.")

(define-room :park "Neighborhood Park"
  {:e :street}
  "You're in the park.  You can tell it's the park because of the statue
  in the middle and the low wrought iron fence all of the way around.
  But it's still gritty.  And smelly.")

