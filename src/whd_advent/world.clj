;;;; world.clj
;;;;
;;;; World definitions for Will's Text Adventure

(ns whd-advent.world
  (:require [clojure.string :as str])
  (:use whd-advent.tools)
  (:use whd-advent.room)
  (:use whd-advent.thing))

;;; ## World Map
;;;
;;; This is the world map data

(define-room :home "Home Base"
  {:n :street}
  ["Your home base is the picture of comfort, from the deep
   pile shag carpeting to the oversized leather couch."
   [[:tv :broken] 
    "Still, you'd be much happier if your big-screen TV was working."]]
  :contents [:sofa :tv :ball])

(define-thing :sofa "battered sofa"
  "Your sofa is comfortable, but it looks like it has seen better days."
  :furniture true
  :contents [:house-key])

(define-thing :tv "big-screen TV"
  ["It's a great big TV."
   [[:tv :broken] "Pity about the crack in the screen."]]
  :furniture true)

(define-thing :ball "soccer ball" 
  "It's a soccer ball.  Black, white, you know.")

(define-thing :house-key "house key"
  "Your house key.")


(define-room :street "The Street"
  {:n :pub :s :home :e :corner :w :park}
  ["It's gritty here in the mean streets."
   [[:sewer :clogged] "Also smelly, but a bath or two might fix that."]
   "Anyway, it's no place to stay for long."])

(define-room :corner "The Corner"
  {:w :street}
  "You're down on the corner.  You think about hanging out for a while,
  but it's no fun without company.")

(define-room :park "Neighborhood Park"
  {:e :street}
  ["You're in the park.  You can tell it's the park because of the statue
    in the middle and the low wrought iron fence all of the way around.
    But it's still gritty."
   [[:sewer :clogged] "And smelly.  Maybe it isn't you."]])

(define-room :pub "The Local Pub"
  {:s :street}
  ["This is the local pub."
   [[:sewer :clogged] "It smells like beer, which is an improvement."]
   "You wave at the locals, and they wave back."])



