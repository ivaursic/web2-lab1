# Prvi stage: Maven build
FROM maven:3.8.5-openjdk-17 as build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Drugi stage: Pokretanje aplikacije
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/lab1-0.0.1-SNAPSHOT.jar /lab1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/lab1.jar"]
