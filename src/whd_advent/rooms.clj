;;;; rooms.clj
;;;;
;;;; Actual room definitions for Will's Text Adventure

(ns whd-advent.rooms
  (:require [clojure.string :as str])
  (:use whd-advent.room))

;;; ## World Map
;;;
;;; This is the world map data

(define-room :home "Home Base" 
  {:n :street}
  "Your home base is the picture of comfort.")

(define-room :street "The Street" 
  {:s :home :e :corner :w :park}
  "It's gritty here in the mean streets.")

(define-room :corner "The Corner"
  {:w :street}
  "You're down on the corner.")

(define-room :park "Neighborhood Park"
  {:e :street}
  "You're in the park.")

