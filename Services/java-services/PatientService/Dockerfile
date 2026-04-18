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

COPY PatientService/pom.xml PatientService/
COPY PatientService/patient-domain/pom.xml PatientService/patient-domain/
COPY PatientService/patient-application/pom.xml PatientService/patient-application/
COPY PatientService/patient-infrastructure/pom.xml PatientService/patient-infrastructure/
COPY PatientService/patient-api/pom.xml PatientService/patient-api/

RUN mvn -f PatientService/pom.xml dependency:go-offline -B -DskipTests || true

COPY PatientService/patient-domain/src PatientService/patient-domain/src
COPY PatientService/patient-application/src PatientService/patient-application/src
COPY PatientService/patient-infrastructure/src PatientService/patient-infrastructure/src
COPY PatientService/patient-api/src PatientService/patient-api/src

RUN mvn -f PatientService/pom.xml clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine AS final
WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=builder /app/PatientService/patient-api/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

EXPOSE 8080
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
