
at_box(BoxX, BoxY) :- pos(AgX, AgY) & jia.distance(AgX, AgY, BoxX, BoxY, 1).

+!get_box(BoxX, BoxY) : at_box(BoxX, BoxY).

+!get_box(BoxX, BoxY) : pos(AgX, AgY) & jia.directions(AgX, AgY, BoxX, BoxY, 1, Directions) <-
	.print("Agent at (", AgX, ",", AgY, ") get box at (", BoxX, ",", BoxY, ")");
	.print("Directions to box: ", Directions);
	!move(Directions).

-!get_box(BoxX, BoxY) : pos(AgX, AgY) & jia.solve_dependencies(AgX, AgY, BoxX, BoxY) <- 
	+need_help;
	.print("HELP").