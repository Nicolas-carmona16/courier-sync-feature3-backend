# Usar OpenJDK 21 como imagen base
FROM eclipse-temurin:21-jdk

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x ./mvnw

# Descargar dependencias (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN ./mvnw package -DskipTests

# Exponer el puerto 8080 (puerto estándar para Render)
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "target/couriersync-backend-0.0.1-SNAPSHOT.jar"]
