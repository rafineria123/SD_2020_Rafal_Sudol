FROM eclipse-temurin:17-jdk-alpine
COPY target/project-0.0.1-SNAPSHOT.jar /app/project-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/project-0.0.1-SNAPSHOT.jar"]
