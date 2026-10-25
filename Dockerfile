# Stage 1: Build the application (using Java 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application (using Java 21 JRE)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create a non-root system user for security best practices
RUN groupadd -g 1001 finsecure && useradd -u 1001 -g finsecure -ms /bin/bash appuser
USER appuser

COPY --from=build --chown=appuser:finsecure /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]