FROM maven:3.9.6 AS build

RUN mkdir /code

COPY . /code

WORKDIR /code

# Build the application
RUN mvn clean package -DskipTests

# Use Adoptium JDK 16 as the base image for the final image
FROM adoptopenjdk/openjdk16

RUN mkdir /app

# Copy the JAR file from the Maven image

COPY --from=build /code/target/gpu-configurator-0.0.1-SNAPSHOT.jar /app/gpuconfigurator.jar

# Set the working directory in the container
WORKDIR /app

# Specify the command to run on container startup
# CMD ["java", "-jar", "your-application.jar"]
CMD java $JAVA_OPTS -jar gpuconfigurator.jar