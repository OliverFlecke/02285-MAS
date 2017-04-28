{ include("agent_moving.asl") }
{ include("box_getting.asl") }
{ include("box_moving.asl") }

free_goals :- color(C) & not (goal(L, X, Y) & box(C, L, X, Y)).

!do_action.

+!do_action : not help <- !solve_level; !do_action.

+!do_action <- .wait(100); !do_action.
-!do_action <- .wait(100); !do_action.

+helped[source(Agent)] : need_help <- -helped; .print("Help received from ", Agent); -need_help; !solve_level; .send(Agent, tell, help_done); !solve_level.

+!help_done_wait : help_done	<- !solve_level.
+!help_done_wait 				<- skip; !help_done_wait.
																														// Send the message only to the requester
+help(ToX, ToY) 			<- .drop_all_intentions; +help; .print("HELP MOVE"); !move(ToX, ToY)					; .broadcast(tell, helped); -help; !help_done_wait.
+help(BoxX, BoxY, ToX, ToY) <- .drop_all_intentions; +help; .print("HELP BOX "); !move_box(BoxX, BoxY, ToX, ToY) 	; .broadcast(tell, helped); -help; !help_done_wait.	

+!solve_level : not need_help & pos(AgX, AgY) & free_goals <- 
	jia.select_goal(AgX, AgY, BoxX, BoxY, GoalX, GoalY);
	!move_box(BoxX, BoxY, GoalX, GoalY).
+!solve_level <- !wait.
-!solve_level <- .print("Skipping"); skip.

+!wait <- skip; !wait.
