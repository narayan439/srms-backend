# ===============================
# Build stage
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# If pom.xml is inside src/
COPY src/pom.xml ./pom.xml
RUN mvn dependency:go-offline

# Copy source
COPY src ./src

# Build Spring Boot JAR
RUN mvn clean package -DskipTests

# ===============================
# Runtime stage (Spring Boot)
# ===============================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]
