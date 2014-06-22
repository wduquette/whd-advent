# Brainstorming File

Take nothing here too seriously.  It's either wrong, out-of-date, or in
the process of being overtaken by events.

## Describing things.

* Describing rooms, things, and characters should all be similar
  things.  I probably want one set of code to do that.

* There are multiple "hooks" I might want to use to describe something.
  I have two at present, one where it's just one string, and one
  where I can specify a sequence of strings, some conditioned on
  facts.

* Sounds like a case for multi-methods.  The whatsit function
  determines how the description text is to be computed, and then
  the routine does it.

* The coder doesn't need to specify a hook function.
