# whd-advent

Will's Text Adventure is meant to be a simple Infocom-like text adventure
game.  I'm writing it as an exercise in learning to program in Clojure.

If you look at the code and ask, "Why on earth is he doing *that*? 
He ought to be doing *this* instead!" then by all means write me
an issue!

## Usage

The game is a simple console application.  To build and run it:

1. Install the latest version of Leiningen.
2. In the project root directory, enter

    lein run

## Plans

The next step is to define inventories.  An inventory is a (probably sorted) 
set of things associated with some entity, and also the player.  We might
also want a :limbo inventory, for objects that will appear later.

Rooms and things can have a :contents; this is a vector of the things
located there at the start of the game.  Before the game begins, a function
will assign the initial contents of things to their inventories, which are
held separately as part of the mutable game state.

## Display

Consider building a Swing GUI that has a command line at the bottom, and
shows the current room in HTML at the top.  Then format HTML instead of
wrapping text strings.  Could be a lot of fun.

The [SwingBox](http://cssbox.sourceforge.net/swingbox/download.php) might
be useful, if a JEditorPane won't do the job.
