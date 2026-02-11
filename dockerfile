# USAR ESTA IMAGEM (Eclipse Temurin é a recomendada para Java 21)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]