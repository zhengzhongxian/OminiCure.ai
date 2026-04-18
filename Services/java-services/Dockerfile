FROM maven:3.9.6-eclipse-temurin-17-alpine AS core-builder
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

# ==============================================================================
# IAM Service
# ==============================================================================
FROM core-builder AS iam-builder

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

# ==============================================================================
# Patient Service
# ==============================================================================
FROM core-builder AS patient-builder

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

# ==============================================================================
# IAM Runtime
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine AS iam-service
WORKDIR /app
RUN apk add --no-cache curl
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup
COPY --from=iam-builder /app/IAMService/iam-api/target/*.jar app.jar
RUN chown appuser:appgroup app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]

# ==============================================================================
# Patient Runtime
# ==============================================================================
FROM eclipse-temurin:17-jre-alpine AS patient-service
WORKDIR /app
RUN apk add --no-cache curl
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup
COPY --from=patient-builder /app/PatientService/patient-api/target/*.jar app.jar
RUN chown appuser:appgroup app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
