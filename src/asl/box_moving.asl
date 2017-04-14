
+!move_box(BoxX, BoxY, ToX, ToY) : BoxX == ToX & BoxY == ToY.

// The box(X,Y) predicate is used to keep track of the box being moved
+!move_box(BoxX, BoxY, ToX, ToY) <-
	-+box(BoxX, BoxY);
	.print("Move box at (", BoxX, ",", BoxY, ") to (", ToX, ",", ToY, ")");
	jia.directions(BoxX, BoxY, ToX, ToY, 0, Directions);
	.print("Move box directions: ", Directions);
	!move_box(Directions).

+!move_box([]).
+!move_box([Dir1,Dir2|Directions]) <- !move_box(Dir1,Dir2); !move_box([Dir2|Directions]).
+!move_box([Dir		 |Directions]) <- !move_box(Dir); 		!move_box(Directions).

// To make sure that the agent does not block his own path, he should always try to pull other directions than Dir2 first

// Pulling right followed by down
+!move_box(right, down ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY-1) <- pull(up   , left ); -+box(BoxX+1, BoxY).
+!move_box(right, down ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+2, BoxY  ) <- pull(right, left ); -+box(BoxX+1, BoxY).
// Pulling right followed by up
+!move_box(right, up   ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY+1) <- pull(down , left ); -+box(BoxX+1, BoxY).
+!move_box(right, up   ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+2, BoxY  ) <- pull(right, left ); -+box(BoxX+1, BoxY).
// Pulling right followed by right
+!move_box(right, right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY+1) <- pull(down , left ); -+box(BoxX+1, BoxY).
+!move_box(right, right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY-1) <- pull(up   , left ); -+box(BoxX+1, BoxY).

// Pulling left followed by down
+!move_box(left , down ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY-1) <- pull(up   , right); -+box(BoxX-1, BoxY).
+!move_box(left , down ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-2, BoxY  ) <- pull(left , right); -+box(BoxX-1, BoxY).
// Pulling left followed by up
+!move_box(left , up   ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY+1) <- pull(down , right); -+box(BoxX-1, BoxY).
+!move_box(left , up   ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-2, BoxY  ) <- pull(left , right); -+box(BoxX-1, BoxY).
// Pulling left followed by left
+!move_box(left , left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY+1) <- pull(down , right); -+box(BoxX-1, BoxY).
+!move_box(left , left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY-1) <- pull(up   , right); -+box(BoxX-1, BoxY).

// Pulling up followed by right
+!move_box(up   , right) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX-1, BoxY-1) <- pull(left , down ); -+box(BoxX, BoxY-1).
+!move_box(up   , right) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX  , BoxY-2) <- pull(up   , down ); -+box(BoxX, BoxY-1).
// Pulling up followed by left
+!move_box(up   , left ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX+1, BoxY-1) <- pull(right, down ); -+box(BoxX, BoxY-1).
+!move_box(up   , left ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX  , BoxY-2) <- pull(up   , down ); -+box(BoxX, BoxY-1).
// Pulling up followed by up
+!move_box(up   , up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX+1, BoxY-1) <- pull(right, down ); -+box(BoxX, BoxY-1).
+!move_box(up   , up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX-1, BoxY-1) <- pull(left , down ); -+box(BoxX, BoxY-1).

// Pulling down followed right
+!move_box(down , right) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX-1, BoxY+1) <- pull(left , up   ); -+box(BoxX, BoxY+1).
+!move_box(down , right) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX  , BoxY+2) <- pull(down , up   ); -+box(BoxX, BoxY+1).
// Pulling down followed left
+!move_box(down , left ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX+1, BoxY+1) <- pull(right, up   ); -+box(BoxX, BoxY+1).
+!move_box(down , left ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX  , BoxY+2) <- pull(down , up   ); -+box(BoxX, BoxY+1).
// Pulling down followed down
+!move_box(down , down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX+1, BoxY+1) <- pull(right, up   ); -+box(BoxX, BoxY+1).
+!move_box(down , down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX-1, BoxY+1) <- pull(left , up   ); -+box(BoxX, BoxY+1).

// Do move_box/1 if no plans succeed.
+!move_box(Dir  , _    ) <- !move_box(Dir).

//                  Agent to the right of box             Space at new agent pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY+1) <- pull(down , left ); -+box(BoxX+1, BoxY).
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+1, BoxY-1) <- pull(up   , left ); -+box(BoxX+1, BoxY).
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX+2, BoxY  ) <- pull(right, left ); -+box(BoxX+1, BoxY).
//                  Agent to the left of box              Space at new box pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX+1, BoxY  ) <- push(right, right); -+box(BoxX+1, BoxY).
//                  Agent below the box                   Space at new box pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX+1, BoxY  ) <- push(up   , right); -+box(BoxX+1, BoxY).
//                  Agent above the box                   Space at new box pos
+!move_box(right) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX+1, BoxY  ) <- push(down , right); -+box(BoxX+1, BoxY).

//                  Agent to the left of box              Space at new agent pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY+1) <- pull(down , right); -+box(BoxX-1, BoxY).
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-1, BoxY-1) <- pull(up   , right); -+box(BoxX-1, BoxY).
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX-2, BoxY  ) <- pull(left , right); -+box(BoxX-1, BoxY).
//                  Agent to the right of box             Space at new box pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX-1, BoxY  ) <- push(left , left ); -+box(BoxX-1, BoxY).
//                  Agent below the box                   Space at new box pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX-1, BoxY  ) <- push(up   , left ); -+box(BoxX-1, BoxY).
//                  Agent above the box                   Space at new box pos
+!move_box(left ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX-1, BoxY  ) <- push(down , left ); -+box(BoxX-1, BoxY).

//                  Agent above the box                   Space at new agent pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX+1, BoxY-1) <- pull(right, down ); -+box(BoxX, BoxY-1).
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX-1, BoxY-1) <- pull(left , down ); -+box(BoxX, BoxY-1).
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX  , BoxY-2) <- pull(up   , down ); -+box(BoxX, BoxY-1).
//                  Agent below the box                   Space at new box pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX  , BoxY-1) <- push(up   , up   ); -+box(BoxX, BoxY-1).
//                  Agent to the right of box             Space at new box pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX  , BoxY-1) <- push(left , up   ); -+box(BoxX, BoxY-1).
//                  Agent to the left of box              Space at new box pos
+!move_box(up   ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX  , BoxY-1) <- push(right, up   ); -+box(BoxX, BoxY-1).

//                  Agent below the box                   Space at new agent pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX+1, BoxY+1) <- pull(right, up   ); -+box(BoxX, BoxY+1).
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX-1, BoxY+1) <- pull(left , up   ); -+box(BoxX, BoxY+1).
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY+1) & free(BoxX  , BoxY+2) <- pull(down , up   ); -+box(BoxX, BoxY+1).
//                  Agent above the box                   Space at new box pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX, BoxY-1) & free(BoxX  , BoxY+1) <- push(down , down ); -+box(BoxX, BoxY+1).
//                  Agent to the right of box             Space at new box pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX+1, BoxY) & free(BoxX  , BoxY+1) <- push(left , down ); -+box(BoxX, BoxY+1).
//                  Agent to the left of box              Space at new box pos
+!move_box(down ) : box(BoxX, BoxY) & pos(BoxX-1, BoxY) & free(BoxX  , BoxY+1) <- push(right, down ); -+box(BoxX, BoxY+1).