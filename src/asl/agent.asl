
pos(1, 2).

// 
last_dir(null).

color(blue).

box(blue, a, 2, 2).

goal(a, 3, 2).

!move_to(12,6).
	
+!solve_goal :
	color(C) & box(C, A, BoxX, BoxY) & goal(A, GoalX, GoalY) <-
	!move_to(BoxX, BoxY);
	!push(BoxX, BoxY, GoalX, GoalY).
	
// Already adjacent to destination
+!move_to(ToX, ToY) :
	pos(AgX, AgY) & jia.distance(AgX, AgY, ToX, ToY, 1).
	
// Use A* to get directions to an adjacent cell
+!move_to(ToX, ToY) :
	pos(AgX, AgY) <-
	.print("Getting directions")
	jia.directions(AgX, AgY, ToX, ToY, Directions);
	.print("Directions: ", Directions);
	for (.member(Dir, Directions)) {
		move(Dir);
	}.
	
// Failed to move
-!move_to(ToX, ToY) :
	pos(AgX, AgY) <-
	.print("Failed at move_to from: (", AgX, ",", AgY, ") to: (", ToX, ",", ToY, ")").

+!push(BoxX, BoxY, GoalX, GoalY) <-
	jia.box_to_goal(BoxX, BoxY, GoalX, GoalY, Dir1, Dir2);
	push(Dir1, Dir2).
	
+cell(Obj,X,Y) : not(Obj == empty) & not(Obj == obstacle) <- 
	.print(Obj, " at (", X, ",", Y, ")").
	
	
	