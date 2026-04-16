FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY CoreDomain/pom.xml CoreDomain/pom.xml
COPY CoreInfra/pom.xml CoreInfra/pom.xml
COPY CoreWebShared/pom.xml CoreWebShared/pom.xml

RUN mvn dependency:go-offline -B

COPY CoreDomain/src CoreDomain/src
COPY CoreInfra/src CoreInfra/src
COPY CoreWebShared/src CoreWebShared/src

RUN mvn clean install -DskipTests -B

FROM eclipse-temurin:17-jre-alpine AS final
WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=builder /app/CoreWebShared/target/*.jar core.jar

RUN chown appuser:appgroup core.jar

USER appuser

ENTRYPOINT ["java", "-jar", "core.jar"]
