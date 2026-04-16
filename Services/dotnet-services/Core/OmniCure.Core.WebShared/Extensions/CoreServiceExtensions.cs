using Microsoft.Extensions.DependencyInjection;

namespace OmniCure.Core.WebShared.Extensions;

public static class CoreServiceExtensions
{
    public static IServiceCollection AddCoreServices(this IServiceCollection services)
    {
        services.AddPolicies();
        return services;
    }
}
