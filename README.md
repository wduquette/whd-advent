# whd-advent

Will's Text Adventure is meant to be a simple Infocom-like text adventure
game.  I'm writing it as an exercise in learning to program in Clojure.

## Usage

The game is a simple console application.  To build and run it:

1. Install the latest version of Leiningen.
2. In the project root directory, enter

    lein run

## Plans

The next step is to add objects for the player to pick up and manipulated.
These will be called "things".

The "room description hook" apparatus can probably be generalized for use by
rooms as well.  In that case, I might want to define a hierarchy:  Room and 
Thing, which are both GameEntities, and which can be described in the same way. 
Similarly, I might want an Inventory hierarchy, where both the Player and
Rooms are Inventories.  We'll see.
