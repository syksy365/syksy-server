FROM openjdk:11-jdk-alpine
VOLUME /tmp
COPY target/alpha-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]