{ include("agent_moving.asl") }
{ include("box_getting.asl") }
{ include("box_moving.asl") }
{ include("box_storing.asl") }

select_box(C, A, GoalX, GoalY, X, Y) :- jia.select_box(C, A, GoalX, GoalY, X, Y).
select_box(C, A, _, _, X, Y) :- box(C, A, X, Y) & not goal(A, X, Y).

select_goal(Letter, X, Y) :- color(C) & pos(AgentX, AgentY) & jia.select_goal(C, Letter, AgentX, AgentY, X, Y).
select_goal(Letter, X, Y) :- goal(Letter, X, Y) & not box(_, Letter, X, Y).

object(goal, 1).
object(box,  8).

// Initial goal
!solve_level.

+!solve_level <-
	while (select_goal(Letter, X, Y))
	{
//		.print("goal(", Letter, ", ", X, ", ", Y, ")");
		!solve_goal(Letter, X, Y);
	}.
//	for ( goal(A, GoalX, GoalY) ) {
//		.print("goal(", A, ", ", GoalX, ", ", GoalY, ")");
//		!solve_goal(A, GoalX, GoalY);
//	}.
	
+!solve_goal(A, X, Y) : box(_, A, X, Y) <- .print("Box on goal").

+!solve_goal(A, X, Y) : color(C) & select_box(C, A, X, Y, BoxX, BoxY) <-
//+!solve_goal(A, X, Y) : color(C) & box(C, A, BoxX, BoxY) & not goal(A, BoxX, BoxY) <-
	.print("Solve goal at (", X, ", ", Y, ") with box at (", BoxX, ",", BoxY, ")");
	!get_box(BoxX, BoxY);
	!move_box(BoxX, BoxY, X, Y).	
	

	


	

	
	
	