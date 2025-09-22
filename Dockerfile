FROM gradle:8.10.1-jdk17-jammy AS builder
WORKDIR /app
COPY build.gradle gradlew ./
RUN chmod +x gradlew
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew build -x test --no-daemon

# Jammy to support amd64 and arm64 architectures
FROM eclipse-temurin:17-jre-jammy AS runner
WORKDIR /app

# Copy generated JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# JVM resource limits and limit to 75% of available memory
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
