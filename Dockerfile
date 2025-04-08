# Establecer la imagen base
FROM maven:3.8.5-openjdk-17-slim AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /workspace/app

# Copia los archivos de proyecto (pom.xml y código fuente)
COPY pom.xml .
COPY /src ./src

# Construye el proyecto y empaqueta en un archivo JAR
RUN mvn clean package -DskipTests

# Etapa de construcción de la imagen final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el JAR generado desde la etapa de construcción
COPY --from=build /workspace/app/target/*.jar app.jar

# Expone el puerto de la aplicación
EXPOSE 8080

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar", "--spring.profiles.active=prod"]