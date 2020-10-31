FROM openjdk:8-jdk-alpine AS builder

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml 
COPY src src

RUN ./mvnw package

FROM openjdk:8-jdk-alpine
COPY --from=builder target/bitzy-0.1.0.jar bitzy.jar
CMD ["java", "-jar", "bitzy.jar" ] 