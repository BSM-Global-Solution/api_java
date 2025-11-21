# Etapa 1: Build do aplicativo
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
RUN chmod +x mvnw

COPY src src
RUN ./mvnw clean package -Dquarkus.package.type=uber-jar -DskipTests

# Etapa 2: Imagem final (runtime)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o Uber Jar gerado
COPY --from=build /app/target/*-runner.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]