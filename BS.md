# Brainstorming File

Take nothing here too seriously.  It's either wrong, out-of-date, or in
the process of being overtaken by events.

## Questions about patterns

Rooms and Things follow the same pattern.  I've defined the entity module
to abstract it.  

Q: Is there an existing Clojure abstraction I should use?


## Things and inventories

Things can be in rooms.  How do they get there?

* Assigned to initial room on definition of thing
* Assigned to initial room on definition of room
* Assigned to initial room as part of initial set up?

Inventory is truly dynamic, where room and thing definitions are not.
Could define initial locations using one routine, that can also be used
when starting a new game; also could associate initial content with the
rooms and build inventories automatically.

"Furniture" things exist in rooms for the purpose of being examined and
otherwise interacted with; they cannot be taken.

There are a number of kinds of thing that have inventories: rooms, the player,
other mobs, and things like boxes and desks.

For command processing, we need to know what objects are visible in a room,
and what vocabulary can be used to refer to them.  The :sofa, the [:red :key].

We need to able to move things from one inventory to another.  