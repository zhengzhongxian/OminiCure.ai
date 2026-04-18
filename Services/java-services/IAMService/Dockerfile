FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

COPY Core/pom.xml Core/
COPY Core/CoreShared/pom.xml Core/CoreShared/
COPY Core/CoreDomain/pom.xml Core/CoreDomain/
COPY Core/CoreApplication/pom.xml Core/CoreApplication/
COPY Core/CoreInfra/pom.xml Core/CoreInfra/
COPY Core/CoreWebShared/pom.xml Core/CoreWebShared/

RUN mvn -f Core/pom.xml dependency:go-offline -B -DskipTests || true

COPY Core/CoreShared/src Core/CoreShared/src
COPY Core/CoreDomain/src Core/CoreDomain/src
COPY Core/CoreApplication/src Core/CoreApplication/src
COPY Core/CoreInfra/src Core/CoreInfra/src
COPY Core/CoreWebShared/src Core/CoreWebShared/src

RUN mvn -f Core/pom.xml clean install -DskipTests -B

COPY IAMService/pom.xml IAMService/
COPY IAMService/iam-domain/pom.xml IAMService/iam-domain/
COPY IAMService/iam-application/pom.xml IAMService/iam-application/
COPY IAMService/iam-infrastructure/pom.xml IAMService/iam-infrastructure/
COPY IAMService/iam-api/pom.xml IAMService/iam-api/

RUN mvn -f IAMService/pom.xml dependency:go-offline -B -DskipTests || true

COPY IAMService/iam-domain/src IAMService/iam-domain/src
COPY IAMService/iam-application/src IAMService/iam-application/src
COPY IAMService/iam-infrastructure/src IAMService/iam-infrastructure/src
COPY IAMService/iam-api/src IAMService/iam-api/src

RUN mvn -f IAMService/pom.xml clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine AS final
WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=builder /app/IAMService/iam-api/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

EXPOSE 8080
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
