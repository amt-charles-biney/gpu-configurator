FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app/
COPY . /app/
RUN mvn clean package -DskipTests
#
# Package stage
#
FROM eclipse-temurin:21-jdk
COPY --from=build /app/target/gpuconfigurator.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]