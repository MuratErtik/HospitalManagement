# --------------------
# Stage 1: Build stage
# --------------------
FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /app

#just copy pom.xml and cache dependency
COPY pom.xml .
RUN mvn dependency:go-offline -B

#copy project files and built those
COPY src ./src
RUN mvn clean package -DskipTests

# --------------------
# Stage 2: Runtime stage
# --------------------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
