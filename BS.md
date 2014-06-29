# Brainstorming File

Take nothing here too seriously.  It's either wrong, out-of-date, or in
the process of being overtaken by events.

## Subsystems

Some thoughts about the proper order to break things down into subsystems.

1. Fundamental Utilities
   1. tools.clj -- Basic Utility Commands.
2. Basic Modules and Data Types, with no coupling
   1. facts.clj -- The fact base is a nice, self-contained module.
3. Game Entities -- TODO: Probably want a hierarchy.
   1. entity.clj -- Definition and behavior of entities in general
   2. room.clj -- Definition and behavior of room entities
   3. thing.clj -- Definition and behavior of thing entities
4. Game World 
   1. world.clj -- Defines the game entities and the initial state of
      of the game world.  The data defined here is static once defined.
   2. vocab.clj -- The vocabulary the player can use depends on the
      things that are defined.
5. core.clj -- This should just be the setup and main routine.  What
   else is currently in here?
   1. @player
   2. @inventory
   3. Queries and actions based on these.
   4. Command translator.  Requires knowledge of vocab and actions.
   5. Mainline code

Suggestions:

* Move @player, @inventory, and the queries and actions based on them
  into a new module, state.clj.

* Move the command translator into a new module, command.clj.
  It should define the commands so that they can be parsed, and then
  translate from sentences to commands.

* Consider changing vocab.tcl to parser.tcl
  * Functions to define terms of different parts of speech:
    * Verbs
    * Nouns
    * Prepositions
    * Noise words (i.e, "the", "a").
  * Code to parse sentences corresponding to basic patterns
    * [verb]
    * [verb noun-phrase]
    * [verb noun-phrase prep-phrase]

* The command translator then:
  * Defines commands: verbs with sentence patterns, related to actions.
  * Defines relevant vocabulary.

* Then, command translation involves:
  * Parsing the input sentence
  * Matching nouns to things in the room.
    * See below.
  * Matching verb and sentence pattern to a known command
  * Returning the command for execution.

## Questions about patterns

Rooms and Things follow the same pattern.  I've defined the entity module
to abstract it.  

Q: Is there an existing Clojure abstraction I should use?


## Command processing and things

We'll need a notion of noun phrases, an adjective and a noun.
Adjective and noun _strings_ will map to keywords in the vocab namespace.
Each object will have associated with it a noun keyword, and possibly an
adjective keyword.  Its ID might in fact be a vector of these, i.e.,
[:red :key] is the ID of the red key.  But honestly, I think I'd rather
decouple these.  Thing IDs are always one keyword, and a thing has
a noun phrase (or more than one) that refer to it.

Then for a given room we need to be able to build a list of the noun phrases
that make sense in the context of the room.  And the command parser will need
to know the vocabulary on the one hand, and the content of the room, so that it
can match the two up.

Commands, then, consist of a pattern: 

* verb
* verb noun-phrase
* verb noun-phrase location

So parsing is all about taking something like 

    put the orange shirt on the wooden table
    
and translating it first into vocabulary

	:put [:shirt :orange] [:table :wooden]

and then into references to specific things

    :put :orange-shirt :wooden-table

which is then something an action can handle.

The necessary point is that incomplete wordings can still match things.

    :put [?noun :orange] [:table ?adjective]

If these wild-carded phrases match single things, that's good enough.
