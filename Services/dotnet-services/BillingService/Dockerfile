# syntax=docker/dockerfile:1

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS restore
WORKDIR /src

COPY ["src/BillingService.Domain/BillingService.Domain.csproj", "src/BillingService.Domain/"]
COPY ["src/BillingService.Application/BillingService.Application.csproj", "src/BillingService.Application/"]
COPY ["src/BillingService.Infrastructure/BillingService.Infrastructure.csproj", "src/BillingService.Infrastructure/"]
COPY ["src/BillingService.API/BillingService.API.csproj", "src/BillingService.API/"]

RUN --mount=type=cache,id=billing-nuget,target=/root/.nuget/packages \
    dotnet restore "src/BillingService.API/BillingService.API.csproj"

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
ARG BUILD_CONFIGURATION=Release
WORKDIR /src

COPY ["src/", "src/"]
COPY --from=restore /src/src/BillingService.Domain/obj/ ./src/BillingService.Domain/obj/
COPY --from=restore /src/src/BillingService.Application/obj/ ./src/BillingService.Application/obj/
COPY --from=restore /src/src/BillingService.Infrastructure/obj/ ./src/BillingService.Infrastructure/obj/
COPY --from=restore /src/src/BillingService.API/obj/ ./src/BillingService.API/obj/

RUN --mount=type=cache,id=billing-nuget-build,target=/root/.nuget/packages \
    dotnet build "src/BillingService.API/BillingService.API.csproj" -c ${BUILD_CONFIGURATION} -o /app/build

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS publish
ARG BUILD_CONFIGURATION=Release
WORKDIR /src

COPY --from=build /app/build ./

RUN dotnet publish "src/BillingService.API/BillingService.API.csproj" \
    -c ${BUILD_CONFIGURATION} \
    -o /app/publish \
    --no-restore \
    --no-build \
    /p:PublishSingleFile=true \
    /p:PublishTrimmed=false \
    /p:SelfContained=false

FROM mcr.microsoft.com/dotnet/aspnet:8.0-alpine AS final
WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=publish /app/publish .

RUN chown -R appuser:appgroup /app

EXPOSE 80
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
    CMD curl -f http://localhost:80/health || exit 1

USER appuser

ENTRYPOINT ["dotnet", "BillingService.API.dll"]
