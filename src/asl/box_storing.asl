
+!store_box([]).
+!store_box([box(X,Y)|Dependencies]) <- store_box(X, Y); !store_box(Dependencies).