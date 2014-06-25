# Brainstorming File

Take nothing here too seriously.  It's either wrong, out-of-date, or in
the process of being overtaken by events.

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
