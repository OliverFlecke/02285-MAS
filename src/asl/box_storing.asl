
//	.print("Store box at (", BoxX, ",", BoxY, ") at (", StrX, ",", StrY, ")");

+!store_box([]).
+!store_box([box(X,Y)|Dependencies]) <-
	jia.storages(X, Y, Storages);
	.print("Found storages: ", Storages);
	!store_box([box(X,Y)|Dependencies], Storages).

+!store_box([],_).
+!store_box([box(BoxX,BoxY)|Dependencies], [storage(StrX,StrY)|Storages]) <- 
	!move_box(BoxX,BoxY,StrX,StrY); 
	!store_box(Dependencies, Storages).
	
-!store_box(_,_) <- .print("Could not find enough storage locations").