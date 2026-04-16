using Microsoft.Extensions.DependencyInjection;
using OmniCure.Core.Domain.Interfaces.Persistence;
using OmniCure.Core.Infrastructure.Persistence.Repositories;

namespace OmniCure.Core.Infrastructure.Extensions;

public static class InfrastructureServiceExtensions
{
    public static IServiceCollection AddCoreInfrastructure(this IServiceCollection services)
    {
        services.AddScoped<IQueryExecutor, QueryExecutor>();
        return services;
    }
}
