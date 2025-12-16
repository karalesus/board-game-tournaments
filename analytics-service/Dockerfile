FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY events-contract/pom.xml ./events-contract/
COPY events-contract/src ./events-contract/src/

COPY board-game-tournaments-api/pom.xml ./board-game-tournaments-api/
COPY board-game-tournaments-api/src ./board-game-tournaments-api/src/

COPY analytics-service/pom.xml ./analytics-service/
COPY analytics-service/src ./analytics-service/src/

RUN mvn -f events-contract/pom.xml clean install -DskipTests && \
    mvn -f board-game-tournaments-api/pom.xml clean install -DskipTests

RUN mvn -f analytics-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Создаем пользователя для безопасности
RUN addgroup -S javauser && adduser -S javauser -G javauser

# Копируем JAR из стадии сборки
COPY --from=builder /build/analytics-service/target/*.jar app.jar

# Меняем владельца файла
RUN chown javauser:javauser /app/app.jar

# Переключаемся на пользователя
USER javauser

# Документируем порт
EXPOSE 8081

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
