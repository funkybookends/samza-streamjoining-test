FROM ewolff/docker-java
RUN apk add --no-cache libstdc++
COPY ./build/libs/user-follows-0.0.1-SNAPSHOT.jar .
CMD /usr/bin/java -Xmx400m -Xms400m -jar user-follows-0.0.1-SNAPSHOT.jar
EXPOSE 11003