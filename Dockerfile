FROM rafineria123/norgieapp
COPY target/project-0.0.1-SNAPSHOT.jar /app/project-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/project-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
EXPOSE 80
