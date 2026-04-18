using System.Reflection;
using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Application.Extensions;
using OmniCure.Core.Domain.Interfaces.Persistence;
using OmniCure.Core.Infrastructure.Extensions;
using OmniCure.Core.Infrastructure.Persistence.Factories;
using OmniCure.Core.Shared.Constants;
using StackExchange.Redis;

namespace OmniCure.Core.WebShared.Extensions;

public static class CoreServiceExtensions
{
    public static IServiceCollection AddOmniCureMicroservice<TWriteDbContext, TReadDbContext>(
        this IServiceCollection services,
        IConfiguration configuration,
        Assembly applicationAssembly,
        string readConnectionStringKey)
        where TWriteDbContext : DbContext
        where TReadDbContext : DbContext
    {
        services.AddPolicies();
        services.AddHttpContextAccessor();

        services.AddCoreApplication(applicationAssembly);

        services.AddCoreInfrastructure<TWriteDbContext, TReadDbContext>();

        var readConnString = configuration.GetConnectionString(readConnectionStringKey) 
            ?? throw new InvalidOperationException($"Connection string '{readConnectionStringKey}' not found.");
        services.AddSingleton<IDbConnectionFactory>(_ => new DbConnectionFactory(readConnString));

        var redisConnString = configuration.GetConnectionString(KeyConstants.ConnectionStrings.Redis) ?? "redis:6379";
        services.AddSingleton<IConnectionMultiplexer>(ConnectionMultiplexer.Connect(redisConnString));

        return services;
    }
}

