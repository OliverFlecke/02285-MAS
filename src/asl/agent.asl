
last_dir(null).

color(blue).

!solve_level.

+!solve_level <-
	for ( goal(A, GoalX, GoalY) ) {
		.print("goal(", A, ", ", GoalX, ", ", GoalY, ")");
		!solve_goal(A, GoalX, GoalY);
	}.
	
+!solve_goal(A, X, Y) : box(_, A, X, Y) <- .print("Box on goal").

+!solve_goal(A, X, Y) : 
	color(C) & box(C, A, BoxX, BoxY) 
	& not goal(A, BoxX, BoxY) <-
	.print("solve_goal(", A, ", ", X, ", ", Y, ") with box at ", BoxX, ",", BoxY);
	!move(BoxX-1, BoxY);
	!push(BoxX, BoxY, X, Y).	

+!move(X, Y) : pos(X, Y) <- .print("move(", X, ", ", Y, ")").

+!move(ToX, ToY) :
	pos(AgX, AgY) <-
	.print("move(", AgX, ", ", AgY, ", ", ToX, ", ", ToY, ")");
	jia.directions(AgX, AgY, ToX, ToY, 0, Directions);
	.print("Move directions: ", Directions);
	for ( .member(Dir, Directions) ) {
		move(Dir);
	}.

+!push(BoxX, BoxY, GoalX, GoalY) <-
	.print("push(", BoxX, ", ", BoxY, ", ", GoalX, ", ", GoalY, ")");
	jia.directions(BoxX, BoxY, GoalX, GoalY, 0, Directions);
	.print("Push directions: ", Directions);
	for ( .member(Dir2, Directions) ) {
		?last_dir(Dir1);
		if (Dir1 == null) {
			push(right, Dir2);
		}
		else {
			push(Dir1, Dir2);
		}
		-+last_dir(Dir2);
	}
	-+last_dir(null).
	
	
	