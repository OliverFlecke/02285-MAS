{ include("agent_moving.asl") }
{ include("box_getting.asl") }
{ include("box_moving.asl") }
{ include("box_storing.asl") }

select_box(C, A, GoalX, GoalY, X, Y) :- jia.select_box(C, A, GoalX, GoalY, X, Y).
select_box(C, A, _, _, X, Y) :- box(C, A, X, Y) & not goal(A, X, Y).

select_goal(Letter, X, Y) :- color(C) & pos(AgentX, AgentY) & jia.select_goal(C, Letter, AgentX, AgentY, X, Y).
select_goal(Letter, X, Y) :- goal(Letter, X, Y) & not box(_, Letter, X, Y).

object(goal, 2).
object(box,  4).

// Initial goal
!solve_level.

//+!solve_level <-
//	while (select_goal(Letter, X, Y)) { !solve_goal(Letter, X, Y); }.
////	for ( goal(A, GoalX, GoalY) ) { !solve_goal(A, GoalX, GoalY); }.
//	
//+!solve_goal(A, X, Y) : box(_, A, X, Y).
//
////+!solve_goal(A, X, Y) : color(C) & box(C, A, BoxX, BoxY) & not goal(A, BoxX, BoxY) <-
//+!solve_goal(A, X, Y) : color(C) & select_box(C, A, X, Y, BoxX, BoxY) <-
//	.print("Solve goal at (", X, ", ", Y, ") with box at (", BoxX, ",", BoxY, ")");
//	!move_box(BoxX, BoxY, X, Y).

	
//+!add_dependencies : object(goal, Obj) <-
//	
//	for ( goal(_, GoalX, GoalY) ) {
//		
//		jia.dependencies(GoalX, GoalY, AgX, AgY, Obj, Dependencies);
//		
//		for ( .member(depend(X, Y), Dependencies)) {
//			
//			?goal(X, Y, List);
//			
//			-+goal(X, Y, [depend(GoalX, GoalY)]);
//		};		
//	}.

+!solve_level : pos(AgX, AgY) & object(goal, Obj) & color(C) <-

	+dependencies([]);
	
	for ( goal(L, GoalX, GoalY) & box(C, L, _, _) ) {
		jia.dependencies(GoalX, GoalY, AgX, AgY, Obj, Dependencies);
		?dependencies(List);
		-+dependencies([depend(GoalX, GoalY,Dependencies)|List]);
	};
	
	?dependencies(List);
	jia.prioritize(List, Goals);
	!solve_goal(Goals).
	
+!solve_goal([]) <- !end.
+!solve_goal([goal(X,Y)|Goals]) : goal(L, X, Y) <- !solve_goal(L, X, Y); !solve_goal(Goals).

+!solve_goal(L, X, Y) : box(_, L, X, Y).
+!solve_goal(L, X, Y) : color(C) & box(C, L, BoxX, BoxY) & not goal(L, BoxX, BoxY) <- !move_box(BoxX, BoxY, X, Y).

+!end <- skip; !end.
