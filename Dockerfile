FROM maven:3.6.3-openjdk-11-slim AS build

RUN mkdir /code
# Copy the Maven POM file and download dependencies
COPY pom.xml .

COPY . /code

WORKDIR /code

# Build the application
RUN mvn clean package

# Use Adoptium JDK 11 as the base image for the final image
FROM adoptopenjdk/openjdk11:jre-11.0.15_10-alpine

RUN mkdir /app

RUN addgroup -g 1001 -S gpugroup

RUN adduser -S maximo -u 1001

# Copy the JAR file from the Maven image

COPY --from=build /code/target/target/gpu-configurator-0.0.1-SNAPSHOT.jar /app/gpuconfigurator.jar

# Set the working directory in the container
WORKDIR /app

RUN chown -R maximo:gpugroup /app

# Specify the command to run on container startup
# CMD ["java", "-jar", "your-application.jar"]
CMD java $JAVA_OPTS -jar gpuconfigurator.jar