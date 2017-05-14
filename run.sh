build.sh
java -jar server.jar -l $1 -c "java -Xmx4g -jar client.jar" -g 100 -t 300 $2
