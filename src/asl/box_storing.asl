
//	.print("Store box at (", BoxX, ",", BoxY, ") at (", StrX, ",", StrY, ")");
//	jia.storages(X, Y, 0, [storage(StrX, StrY)|_]);
//	.print("Found storage at (", StrX, ",", StrY, ")");
//	!move_box(X, Y, StrX, StrY).

+!store_box([]).
+!store_box([depend(X,Y)|Dependencies]) <-
	jia.storages(X, Y, 0, Storages);
	.print("Found storages: ", Storages);
	!store_box([depend(X,Y)|Dependencies], Storages).

+!store_box([],_).
+!store_box([depend(BoxX,BoxY)|Dependencies], [storage(StrX,StrY)|Storages]) <- 
	!move_box(BoxX, BoxY, StrX, StrY); 
	!store_box(Dependencies, Storages).
	
//+!store_box([box(BoxX,BoxY)|Dependencies],_) : object(box, Obj) <- 
//	jia.storages(X, Y, Obj, Storages);
//	.print("Found storages: ", Storages, " These storages may block boxes!");
//	!store_box([box(X,Y)|Dependencies], Storages).
	
-!store_box(Dependencies,_) <- .print("Could not find enough storages for: ", Dependencies).
	
