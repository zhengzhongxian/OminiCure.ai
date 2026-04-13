FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY patient-domain/pom.xml patient-domain/pom.xml
COPY patient-application/pom.xml patient-application/pom.xml
COPY patient-infrastructure/pom.xml patient-infrastructure/pom.xml
COPY patient-api/pom.xml patient-api/pom.xml

RUN mvn dependency:go-offline -B

COPY patient-domain/src patient-domain/src
COPY patient-application/src patient-application/src
COPY patient-infrastructure/src patient-infrastructure/src
COPY patient-api/src patient-api/src

RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine AS final
WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=builder /app/patient-api/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

EXPOSE 8080
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
