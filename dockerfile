# Stage 1: Build the JAR using Maven + JDK 25
FROM maven:3.9.12-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build ()im skipping tests because I dont have any)
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage with slim JRE 25 (smaller image)
FROM eclipse-temurin:25-jre
WORKDIR /app
# Copy only the built JAR
COPY --from=build /app/target/*.jar app.jar
# Expose default Spring Boot port (Render overrides with $PORT if needed)
EXPOSE 8080
# Run the JAR (Spring Boot auto-binds to $PORT env var if set)
ENTRYPOINT ["java", "-jar", "app.jar"]