
//	.print("Store box at (", BoxX, ",", BoxY, ") at (", StrX, ",", StrY, ")");
//	jia.storages(X, Y, 0, [storage(StrX, StrY)|_]);
//	.print("Found storage at (", StrX, ",", StrY, ")");
//	!move_box(X, Y, StrX, StrY).

//+!solve_dependencies(Dependencies) <- !store_box(Dependencies).

+!solve_dependencies([]).
+!solve_dependencies([depend(X,Y)|Dependencies]) <-
	jia.storages(X, Y, 0, Storages);
	.print("Found storages: ", Storages);
	Storages = [Storage | Rest]; // Should properly find a way to select the best storage
	!store_box(depend(X,Y), Storage);
	!solve_dependencies(Dependencies). // Should recursively solve dependencies

//+!store_box([],_).
+!store_box(depend(BoxX,BoxY), storage(StrX,StrY))
	: color(C) & box(C, L, BoxX, BoxY)
	<- !move_box(BoxX, BoxY, StrX, StrY).
+!store_box(depend(BoxX, BoxY), storage(StrX, StrY))
	: box(C, L, BoxX, BoxY) & not color(C)
	<- !get_box(BoxX, BoxY);
	?need_help(depend(BoxX, BoxY)). // Get someone to move the box

// Wait until someone have moved the box for you, then continue
+?need_help(depend(BoxX, BoxY)) : box(_, _, BoxX, BoxY)
	<- skip; ?need_help(depend(BoxX, BoxY)).
+?need_help(depend(X, Y)) : not box(_, _, X, Y) <- 
	.print("Box has been move away ", X, ", ", Y).	

//+!store_box([box(BoxX,BoxY)|Dependencies],_) : object(box, Obj) <- 
//	jia.storages(X, Y, Obj, Storages);
//	.print("Found storages: ", Storages, " These storages may block boxes!");
//	!store_box([box(X,Y)|Dependencies], Storages).
	
-!store_box(Dependencies,_) <- .print("Could not find enough storages for: ", Dependencies).
	
