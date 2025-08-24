# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Package the application into an executable JAR file
RUN mvn clean package -DskipTests

# Stage 2: Create a final, smaller image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory for the final image
WORKDIR /app

# Copy the executable JAR from the build stage to the final image
COPY --from=build /app/target/*.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
