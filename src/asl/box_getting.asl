

+!get_box(BoxX, BoxY) : pos(AgX, AgY) & jia.distance(AgX, AgY, BoxX, BoxY, 1).

+!get_box(BoxX, BoxY) : pos(AgX, AgY) & object(box, Obj) 
	& jia.dependencies(BoxX, BoxY, AgX, AgY, Obj, []) <-
	jia.directions(AgX, AgY, BoxX, BoxY, 1, Directions);
	.print("Directions to box: ", Directions);
	!move(Directions).
	
+!get_box(BoxX, BoxY) 
	: pos(AgX, AgY) & object(box, Obj) & color(C) & box(C, _, BoxX, BoxY)
	<- 
	.print("Agent at (", AgX, ",", AgY, ") get box at (", BoxX, ",", BoxY, ")");
	jia.plan_solve_dependencies(AgX, AgY, BoxX, BoxY);
//	jia.dependencies(BoxX, BoxY, AgX, AgY, Obj, Dependencies);
//	.print("Box dependencies: ", Dependencies);
//	!solve_dependencies(Dependencies);
//	.print("Dependiencise solved; moving on");
	.fail.
	
//-!get_box(BoxX, BoxY) : pos(AgX, AgY) <-
//	.print("Agent at (", AgX, ",", AgY, ") failed to get box at (", BoxX, ",", BoxY, ")").