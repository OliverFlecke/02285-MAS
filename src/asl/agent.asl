+cell(Obj,X,Y) : not(Obj == empty) & not(Obj == obstacle) <- 
//	.print(Obj, " at (", X, ",", Y, ")");
	move(up).
	