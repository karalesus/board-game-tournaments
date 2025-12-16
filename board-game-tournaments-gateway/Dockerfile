FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY events-contract/pom.xml ./events-contract/
COPY events-contract/src ./events-contract/src/

COPY board-game-tournaments-api/pom.xml ./board-game-tournaments-api/
COPY board-game-tournaments-api/src ./board-game-tournaments-api/src/

COPY board-game-tournaments-gateway/pom.xml ./board-game-tournaments-gateway/
COPY board-game-tournaments-gateway/src ./board-game-tournaments-gateway/src/

RUN mvn -f events-contract/pom.xml clean install -DskipTests && \
    mvn -f board-game-tournaments-api/pom.xml clean install -DskipTests

RUN mvn -f board-game-tournaments-gateway/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S javauser && adduser -S javauser -G javauser

COPY --from=builder /build/board-game-tournaments-gateway/target/*.jar app.jar

RUN chown javauser:javauser /app/app.jar

USER javauser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
