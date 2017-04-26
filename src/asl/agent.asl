{ include("agent_moving.asl") }
{ include("box_getting.asl") }
{ include("box_moving.asl") }

!do_action.

+!do_action : not help <- !solve_level; !do_action.

-!do_action <- .wait(1000); !do_action.

+helped <- -helped; .print("Help received"); -need_help.

+help(ToX, ToY) 			<- +help; .print("HELP MOVE"); !move(ToX, ToY)					; .broadcast(tell, helped); +helped; -help.
+help(BoxX, BoxY, ToX, ToY) <- +help; .print("HELP BOX");  !move_box(BoxX, BoxY, ToX, ToY) 	; .broadcast(tell, helped); +helped; -help.
	
+!solve_level : not need_help & pos(AgX, AgY) & color(C) & goal(L, X, Y) & not box(C, L, X, Y) <- 
	jia.select_goal(AgX, AgY, BoxX, BoxY, GoalX, GoalY);
	!move_box(BoxX, BoxY, GoalX, GoalY).
	
-!solve_level <- .print("Skipping"); skip.
