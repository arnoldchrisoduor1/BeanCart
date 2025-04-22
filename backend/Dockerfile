# Build stage
FROM eclipse-temurin:17-jdk-focal AS build
WORKDIR /workspace/app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Configure Maven to use mounted volume
ENV MAVEN_OPTS="-Dmaven.repo.local=/workspace/app/.m2/repository"

COPY pom.xml .
# Use fewer threads and limit memory usage
RUN mvn dependency:go-offline -B -T 1C

# Copy source code and build
COPY src src
RUN mvn package -DskipTests -B -T 1C
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Create a non-root user to run the application
RUN groupadd -r spring && useradd -r -g spring spring

# Copy application from build stage
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Set ownership and permissions
RUN chown -R spring:spring /app
USER spring

# Define health check
HEALTHCHECK --interval=30s --timeout=5s CMD curl -f http://localhost:8000/actuator/health || exit 1

COPY --from=build /workspace/app/target/app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
