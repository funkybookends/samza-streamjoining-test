FROM ewolff/docker-java
COPY ./build/libs/event-producer-1.0-SNAPSHOT.jar .
CMD /usr/bin/java -Xmx400m -Xms400m -jar event-producer-1.0-SNAPSHOT.jar
EXPOSE 11003