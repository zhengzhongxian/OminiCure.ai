using Microsoft.Extensions.DependencyInjection;
using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Application.Common.Interfaces.Persistence;
using OmniCure.Core.Application.Interfaces.Caching;
using OmniCure.Core.Domain.Interfaces.Persistence;
using OmniCure.Core.Domain.Interfaces.Persistence.Repositories;
using OmniCure.Core.Infrastructure.Caching;
using OmniCure.Core.Infrastructure.Persistence.Repositories;

namespace OmniCure.Core.Infrastructure.Extensions;

public static class CoreInfrastructureServiceExtensions
{
    public static IServiceCollection AddCoreInfrastructure<TWriteDbContext, TReadDbContext>(this IServiceCollection services)
        where TWriteDbContext : DbContext
        where TReadDbContext : DbContext
    {
        services.AddScoped<IQueryExecutor, QueryExecutor>();
        services.AddScoped<IDapperRepository, DapperRepository>();
        services.AddScoped<IUnitOfWork, UnitOfWork<TWriteDbContext, TReadDbContext>>();

        services.AddSingleton<ICacheService, CacheService>();
        services.AddSingleton<IRedisSearchService, RedisSearchService>();

        return services;
    }
}
