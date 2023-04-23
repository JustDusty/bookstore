# Use an official Java runtime as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app
# Add the JAR file to the image
COPY SharedLibrary/target/MainModule-1.0.jar .

# Expose the port that the application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "MainModule-1.0.jar"]
