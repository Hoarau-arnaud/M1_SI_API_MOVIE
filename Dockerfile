# Dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app

# copie le jar buildé par Gradle
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]