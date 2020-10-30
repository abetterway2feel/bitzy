FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml 
COPY src src

RUN ./mvnw package && java -jar target/url-shortener-0.1.0.jar


ENTRYPOINT ["java","-jar","/url-shortener-0.1.0.jar"]