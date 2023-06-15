# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} user-service-1.0-SNAPSHOT.jar

# Run the jar file
ENTRYPOINT ["java","-Dspring.datasource.url=jdbc:postgresql://postgres:5432/socialnet", "-Dspring.datasource.username=postgres", "-Dspring.datasource.password=postgres","-jar","/user-service-1.0-SNAPSHOT.jar"]