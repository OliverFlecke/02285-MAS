echo "NOTE: You need to manually recompile the jar before running"
java -Dsun.java2d.opengl=true -jar server.jar -l $1 -c "java -Xmx4g -jar client.jar" -g 500 -t 300 $2
