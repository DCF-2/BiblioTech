# Etapa 1: Define a base (Java 17 leve)
FROM openjdk:21-jdk-slim

# Etapa 2: Cria a pasta de trabalho
WORKDIR /app

# Etapa 3: Copia o programa compilado para dentro do container
COPY target/*.jar app.jar

# Etapa 4: Expõe a porta 8080 (padrão web)
EXPOSE 8080

# Etapa 5: Comando que roda o sistema
ENTRYPOINT ["java", "-jar", "app.jar"]