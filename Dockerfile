FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/document-0.0.1-SNAPSHOT.jar document.jar
ENTRYPOINT ["java","-jar","/document.jar"]