{ include("agent_moving.asl") }
{ include("box_getting.asl") }
{ include("box_moving.asl") }

!do_action.

+!do_action : not help 	<- !solve_level; !do_action.
-!do_action 			<- .wait(100); !do_action.

+helped[source(Agent)] : need_help <- -helped; .print("Help received from ", Agent); -need_help; !solve_level; .send(Agent, tell, help_done); .print("Goal solved"); !solve_level.

+!help_done_wait : help_done	<- .print("Help succesful, continueing"); !solve_level.
+!help_done_wait 				<- .print("Wating for help to be succesful"); skip; !help_done_wait.

+help(ToX, ToY) 			<- 
	.drop_all_intentions; +help; -need_help; 
	.print("HELP MOVE"); 
	!move(ToX, ToY)					; 
	.broadcast(tell, helped); 	// .send instead of .broadcast? Need agent name to do so
	-help; !help_done_wait.
	
+help(BoxX, BoxY, ToX, ToY) <- 
	.drop_all_intentions; +help; 
	.print("HELP BOX "); 
	!move_box(BoxX, BoxY, ToX, ToY) 	; 
	.broadcast(tell, helped); 
	-help; !help_done_wait.	

+!solve_level : not need_help & pos(AgX, AgY) & color(C) & jia.free_goals(C) <- 
	jia.select_goal(AgX, AgY, BoxX, BoxY, GoalX, GoalY);
	!move_box(BoxX, BoxY, GoalX, GoalY).
+!solve_level <- .print("No reachable goals, skipping"); skip.
-!solve_level <- .print("Skipping"); skip.
