# Brainstorming File

Take nothing here too seriously.  It's either wrong, out-of-date, or in
the process of being overtaken by events.

## Questions about patterns

Rooms and Things follow the same pattern.  I've defined the entity module
to abstract it.  

Q: Is there an existing Clojure abstraction I should use?


## Questions about descriptions

The (describe) command knows how to describe an entity given its map.
But it could also describe something given its keyword; after all, all 
game entity keywords should be unique.  However, there's currently no
way to retrieve an arbitrary entity's map.  There could be, though.
(get-entity kw) could do a cond, (is-room? kw), etc., to retrieve it.
Alternatively, all entities could be stored in a single map, with an
entity type keyword, e.g., `:type :room`, `:type :thing`.

The (describe-room) function is an anomaly: it includes the title.  I'd
rather leave the concatenation of the title with the description in 
(describe-surroundings) and the "look" command, and describe all things
with one command.

## Things and inventories

* I'll want thing.clj and things.clj in parallel to room.clj/rooms.clj.

