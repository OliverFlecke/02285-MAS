ant -buildfile project.xml
java -jar server.jar -p -l levels/single_agent/sokoban/SAsoko1_06.lvl -c "java -jar client.jar" -g 50 -t 300
