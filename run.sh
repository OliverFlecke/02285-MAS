ant -buildfile project.xml
java -jar server.jar -l levels/multi_agent/short_solution/MACrunch.lvl -c "java -jar client.jar" -g 50 -t 300
