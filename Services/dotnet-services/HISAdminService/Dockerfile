# syntax=docker/dockerfile:1

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS restore
WORKDIR /src

COPY ["src/HISAdmin.Domain/HISAdmin.Domain.csproj", "src/HISAdmin.Domain/"]
COPY ["src/HISAdmin.Application/HISAdmin.Application.csproj", "src/HISAdmin.Application/"]
COPY ["src/HISAdmin.Infrastructure/HISAdmin.Infrastructure.csproj", "src/HISAdmin.Infrastructure/"]
COPY ["src/HISAdmin.API/HISAdmin.API.csproj", "src/HISAdmin.API/"]

RUN --mount=type=cache,id=hisadmin-nuget,target=/root/.nuget/packages \
    dotnet restore "src/HISAdmin.API/HISAdmin.API.csproj"

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
ARG BUILD_CONFIGURATION=Release
WORKDIR /src

COPY ["src/", "src/"]
COPY --from=restore /src/src/HISAdmin.Domain/obj/ ./src/HISAdmin.Domain/obj/
COPY --from=restore /src/src/HISAdmin.Application/obj/ ./src/HISAdmin.Application/obj/
COPY --from=restore /src/src/HISAdmin.Infrastructure/obj/ ./src/HISAdmin.Infrastructure/obj/
COPY --from=restore /src/src/HISAdmin.API/obj/ ./src/HISAdmin.API/obj/

RUN --mount=type=cache,id=hisadmin-nuget-build,target=/root/.nuget/packages \
    dotnet build "src/HISAdmin.API/HISAdmin.API.csproj" -c ${BUILD_CONFIGURATION} -o /app/build

FROM mcr.microsoft.com/dotnet/sdk:8.0 AS publish
ARG BUILD_CONFIGURATION=Release
WORKDIR /src

COPY --from=build /app/build ./

RUN dotnet publish "src/HISAdmin.API/HISAdmin.API.csproj" \
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

ENTRYPOINT ["dotnet", "HISAdmin.API.dll"]
