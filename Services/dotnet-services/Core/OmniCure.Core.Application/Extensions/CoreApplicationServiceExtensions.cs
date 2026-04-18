using System.Reflection;
using FluentValidation;
using MediatR;
using Microsoft.Extensions.DependencyInjection;
using OmniCure.Core.Application.Behaviors;

namespace OmniCure.Core.Application.Extensions;

public static class CoreApplicationServiceExtensions
{
    public static IServiceCollection AddCoreApplication(this IServiceCollection services, Assembly serviceAssembly)
    {
        services.AddMediatR(cfg =>
        {
            cfg.RegisterServicesFromAssembly(serviceAssembly);
            cfg.AddBehavior(typeof(IPipelineBehavior<,>), typeof(ValidationBehavior<,>));
        });

        services.AddValidatorsFromAssembly(serviceAssembly);

        return services;
    }
}
