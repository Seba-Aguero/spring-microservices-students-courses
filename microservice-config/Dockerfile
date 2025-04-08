FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
COPY .env .env
ENTRYPOINT ["java","-jar","/app/app.jar"]

