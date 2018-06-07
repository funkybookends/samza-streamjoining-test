FROM ewolff/docker-java
COPY ./build/libs/samza-1.0-SNAPSHOT.jar .
CMD /usr/bin/java -Xmx400m -Xms400m -jar samza-1.0-SNAPSHOT.jar
EXPOSE 12003