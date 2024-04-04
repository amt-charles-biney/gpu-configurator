FROM maven:3.11.0-eclipse-temurin-17 AS build
WORKDIR /app/
COPY . /app/
RUN mvn clean package -DskipTests
#
# Package stage
#
FROM eclipse-temurin:17-jdk
COPY --from=build /app/target/gpuconfigurator.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]