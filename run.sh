build.sh
java -jar server.jar -l $1 -c "java -jar client.jar" -g 100 -t 300 $2
