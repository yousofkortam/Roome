FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /app/target/Roome-0.0.1-SNAPSHOT.jar demo.jar

FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

COPY --from=runtime /app/demo.jar .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]