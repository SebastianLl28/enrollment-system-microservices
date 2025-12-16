FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -DskipTests -pl event-store-server -am package

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /workspace/event-store-server/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
