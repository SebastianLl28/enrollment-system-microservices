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
RUN mvn -pl api-gateway -am dependency:go-offline -q --no-transfer-progress

FROM deps AS build
ARG GIT_COMMIT=unknown
ARG GIT_BRANCH=unknown
ARG GIT_COMMIT_TIME=unknown
COPY common/src common/src
COPY api-gateway/src api-gateway/src
RUN printf 'git.branch=%s\ngit.commit.id=%s\ngit.commit.id.full=%s\ngit.commit.id.abbrev=%.7s\ngit.commit.time=%s\ngit.commit.message.short=\ngit.tags=\n' \
    "${GIT_BRANCH}" "${GIT_COMMIT}" "${GIT_COMMIT}" "${GIT_COMMIT}" "${GIT_COMMIT_TIME}" \
    > api-gateway/src/main/resources/git.properties
RUN mvn -pl api-gateway -am package -DskipTests -DskipGitPlugin=true -q --no-transfer-progress

FROM eclipse-temurin:17-jre-jammy
RUN groupadd -r app && useradd -r -g app app
USER app
WORKDIR /app
COPY --from=build /workspace/api-gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
