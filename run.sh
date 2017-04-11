ant -buildfile project.xml
java -jar server.jar -p -l $1 -c "java -jar client.jar" -g 100 -t 300
