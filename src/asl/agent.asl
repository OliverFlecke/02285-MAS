
color(blue).

!solve_level.

+!solve_level <-
	for ( goal(A, GoalX, GoalY) ) {
		.print("goal(", A, ", ", GoalX, ", ", GoalY, ")");
		!solve_goal(A, GoalX, GoalY);
	}.
	
+!solve_goal(A, X, Y) : box(_, A, X, Y) <- .print("Box on goal").

+!solve_goal(A, X, Y) : color(C) & box(C, A, BoxX, BoxY) & not goal(A, BoxX, BoxY) <-
	.print("solve_goal(", A, ", ", X, ", ", Y, ") with box at ", BoxX, ",", BoxY);
	!move_adjacent(BoxX, BoxY);
	!move_box(BoxX, BoxY, X, Y).	

//+!move_to(X, Y) : pos(X, Y) <- .print("move_to(", X, ", ", Y, ")").

+!move_adjacent(ToX, ToY) : pos(AgX, AgY) & jia.distance(AgX, AgY, ToX, ToY, 1).

+!move_adjacent(ToX, ToY) : pos(AgX, AgY) <-
	.print("move_to(", AgX, ", ", AgY, ", ", ToX, ", ", ToY, ")");
	jia.directions(AgX, AgY, ToX, ToY, 1, Directions);
	.print("Move to directions: ", Directions);
	for ( .member(Dir, Directions) ) {
		move(Dir);
	}.
	
+!move_box(BoxX, BoxY, GoalX, GoalY) : BoxX == GoalX & BoxY == GoalY.

// The box(X,Y) predicate is used to keep track of the box being moved
+!move_box(BoxX, BoxY, GoalX, GoalY) <-
	-+box(BoxX, BoxY);
	.print("move_box(", BoxX, ", ", BoxY, ", ", GoalX, ", ", GoalY, ")");
	jia.directions(BoxX, BoxY, GoalX, GoalY, 0, Directions);
	.print("Move box directions: ", Directions);
	for ( .member(Dir, Directions) ) {
		!move_box(Dir);
	}.
	
	
// BOX MOVING RULES // 

//                  Agent to the right of box             Space at new agent pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY+1) <- pull(down , left ); -+box(BoxX+1,BoxY).
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY-1) <- pull(up   , left ); -+box(BoxX+1,BoxY).
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+2, BoxY  ) <- pull(right, left ); -+box(BoxX+1,BoxY).
//                  Agent to the left of box              Space at new box pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX+1, BoxY  ) <- push(right, right); -+box(BoxX+1,BoxY).
//                  Agent below the box                   Space at new box pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX+1, BoxY  ) <- push(up   , right); -+box(BoxX+1,BoxY).
//                  Agent above the box                   Space at new box pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX+1, BoxY  ) <- push(down , right); -+box(BoxX+1,BoxY).

//                  Agent to the left of box              Space at new agent pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY+1) <- pull(down , right); -+box(BoxX-1,BoxY).
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY-1) <- pull(up   , right); -+box(BoxX-1,BoxY).
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-2, BoxY  ) <- pull(left , right); -+box(BoxX-1,BoxY).
//                  Agent to the right of box             Space at new box pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX-1, BoxY  ) <- push(left , left ); -+box(BoxX-1,BoxY).
//                  Agent below the box                   Space at new box pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX-1, BoxY  ) <- push(up   , left ); -+box(BoxX-1,BoxY).
//                  Agent above the box                   Space at new box pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX-1, BoxY  ) <- push(down , left ); -+box(BoxX-1,BoxY).

//                  Agent above the box                   Space at new agent pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX+1, BoxY-1) <- pull(right, down ); -+box(BoxX,BoxY-1).
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX-1, BoxY-1) <- pull(left , down ); -+box(BoxX,BoxY-1).
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX  , BoxY-2) <- pull(up   , down ); -+box(BoxX,BoxY-1).
//                  Agent below the box                   Space at new box pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX  , BoxY-1) <- push(up   , up   ); -+box(BoxX,BoxY-1).
//                  Agent to the right of box             Space at new box pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX  , BoxY-1) <- push(left , up   ); -+box(BoxX,BoxY-1).
//                  Agent to the left of box              Space at new box pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX  , BoxY-1) <- push(right, up   ); -+box(BoxX,BoxY-1).

//                  Agent below the box                   Space at new agent pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX+1, BoxY+1) <- pull(right, up   ); -+box(BoxX,BoxY+1).
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX-1, BoxY+1) <- pull(left , up   ); -+box(BoxX,BoxY+1).
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX  , BoxY+2) <- pull(down , up   ); -+box(BoxX,BoxY+1).
//                  Agent above the box                   Space at new box pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX  , BoxY+1) <- push(down , down ); -+box(BoxX,BoxY+1).
//                  Agent to the right of box             Space at new box pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX  , BoxY+1) <- push(left , down ); -+box(BoxX,BoxY+1).
//                  Agent to the left of box              Space at new box pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX  , BoxY+1) <- push(right, down ); -+box(BoxX,BoxY+1).
	

	
	
	