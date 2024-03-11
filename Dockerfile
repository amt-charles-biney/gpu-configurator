FROM maven:3.9.6 AS build

WORKDIR /code
COPY pom.xml .
COPY . /code

# Build the application cache dependancies
RUN --mount=type=cache,target=/root/.m2 mvn clean package  -DskipTests

# # Build the application
# RUN mvn clean package -DskipTests

# Use Adoptium JDK 16 as the base image for the final image
FROM openjdk:21-jdk-slim

RUN mkdir /app

# Copy the JAR file from the Maven image

COPY --from=build /code/target/gpu-configurator-0.0.1-SNAPSHOT.jar /app/gpuconfigurator.jar

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the Maven image

COPY --from=build /code/target/gpu-configurator-0.0.1-SNAPSHOT.jar /app/gpuconfigurator.jar


EXPOSE 8081

COPY ./init_db.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/init_db.sh

CMD [ "java", "--enable-preview", "-jar", "gpuconfigurator.jar" ]
