FROM maven:3.9.9-eclipse-temurin-17 AS deps
WORKDIR /workspace
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY authorization-server/pom.xml authorization-server/pom.xml
COPY discovery-server/pom.xml discovery-server/pom.xml
COPY enrollment-server/pom.xml enrollment-server/pom.xml
COPY notification-server/pom.xml notification-server/pom.xml
COPY event-store-server/pom.xml event-store-server/pom.xml
RUN mvn -pl discovery-server -am dependency:go-offline -q --no-transfer-progress

FROM deps AS build
COPY discovery-server/src discovery-server/src
RUN mvn -pl discovery-server -am package -DskipTests -q --no-transfer-progress

FROM eclipse-temurin:17-jre-jammy
RUN addgroup -S app && adduser -S app -G app
USER app
WORKDIR /app
COPY --from=build /workspace/discovery-server/target/*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
