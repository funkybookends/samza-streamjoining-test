FROM ewolff/docker-java
RUN apk add --no-cache libstdc++
COPY ./build/libs/user-service-0.0.1-SNAPSHOT.jar .
CMD /usr/bin/java -Xmx400m -Xms400m -jar user-service-0.0.1-SNAPSHOT.jar
EXPOSE 11003