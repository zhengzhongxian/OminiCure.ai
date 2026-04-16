FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY iam-domain/pom.xml iam-domain/pom.xml
COPY iam-application/pom.xml iam-application/pom.xml
COPY iam-infrastructure/pom.xml iam-infrastructure/pom.xml
COPY iam-api/pom.xml iam-api/pom.xml

RUN mvn dependency:go-offline -B

COPY iam-domain/src iam-domain/src
COPY iam-application/src iam-application/src
COPY iam-infrastructure/src iam-infrastructure/src
COPY iam-api/src iam-api/src

RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine AS final
WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=builder /app/iam-api/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

EXPOSE 8080
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
