
//	.print("move(", AgX, ", ", AgY, ", ", ToX, ", ", ToY, ")");
//	.print("Move directions: ", Directions);

+!move(ToX, ToY) : pos(ToX, ToY).

+!move(ToX, ToY) : pos(AgX, AgY) <-
	jia.directions(AgX, AgY, ToX, ToY, 0, Directions);
	!move(Directions).
	
+!move([]).
+!move([Dir|Directions]) : id(Id) <- move(Id, Dir); !move(Directions).