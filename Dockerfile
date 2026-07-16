# Etapa 1: Compilar el proyecto
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar en Tomcat
FROM tomcat:10.1-jdk11
COPY --from=build /app/target/DecorCenterApp.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
