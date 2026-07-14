# ---- build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# cache deps first
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# now copy sources and build
COPY src ./src
RUN mvn -q -DskipTests package

# ---- run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy the built jar
COPY --from=build /app/target/*.jar app.jar

# runtime defaults
ENV SERVER_PORT=8080
EXPOSE 8080

# good container JVM defaults
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]