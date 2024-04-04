FROM maven:3.9.6 AS build
WORKDIR /app/
COPY . /app/
RUN mvn install -DskipTests=false
#
# Package stage
#
FROM openjdk:21-jdk-slim
COPY --from=build /app/target/gpu-configurator-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]