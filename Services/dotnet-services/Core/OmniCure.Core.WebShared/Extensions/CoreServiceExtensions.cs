using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace OmniCure.Core.WebShared.Extensions;

public static class CoreServiceExtensions
{
    public static IServiceCollection AddCoreServices(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddPolicies();
        return services;
    }
}
