# syntax=docker/dockerfile:1

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS restore
WORKDIR /src

COPY ["src/LabService.Domain/LabService.Domain.csproj", "src/LabService.Domain/"]
COPY ["src/LabService.Application/LabService.Application.csproj", "src/LabService.Application/"]
COPY ["src/LabService.Infrastructure/LabService.Infrastructure.csproj", "src/LabService.Infrastructure/"]
COPY ["src/LabService.API/LabService.API.csproj", "src/LabService.API/"]

RUN --mount=type=cache,id=lab-nuget,target=/root/.nuget/packages \
    dotnet restore "src/LabService.API/LabService.API.csproj"

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
ARG BUILD_CONFIGURATION=Release
WORKDIR /src

COPY ["src/", "src/"]
COPY --from=restore /src/src/LabService.Domain/obj/ ./src/LabService.Domain/obj/
COPY --from=restore /src/src/LabService.Application/obj/ ./src/LabService.Application/obj/
COPY --from=restore /src/src/LabService.Infrastructure/obj/ ./src/LabService.Infrastructure/obj/
COPY --from=restore /src/src/LabService.API/obj/ ./src/LabService.API/obj/

RUN --mount=type=cache,id=lab-nuget-build,target=/root/.nuget/packages \
    dotnet build "src/LabService.API/LabService.API.csproj" -c ${BUILD_CONFIGURATION} -o /app/build

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS publish
ARG BUILD_CONFIGURATION=Release
WORKDIR /src

COPY --from=build /app/build ./

RUN dotnet publish "src/LabService.API/LabService.API.csproj" \
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

ENTRYPOINT ["dotnet", "LabService.API.dll"]
