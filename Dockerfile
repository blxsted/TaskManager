# Basis-Image
FROM eclipse-temurin:21-jdk-alpine

# Arbeitsverzeichnis
WORKDIR /app

# Jar ins Image kopieren
COPY target/todoApp-0.0.1-SNAPSHOT.jar app.jar

# Port freigeben
EXPOSE 8080

# Startbefehl
ENTRYPOINT ["java","-jar","app.jar"]
