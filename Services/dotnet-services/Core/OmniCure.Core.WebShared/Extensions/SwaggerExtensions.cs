using Microsoft.Extensions.DependencyInjection;
using Microsoft.OpenApi.Models;

namespace OmniCure.Core.WebShared.Extensions;

public static class SwaggerExtensions
{
    public static IServiceCollection AddOmniCureSwagger(this IServiceCollection services, string apiName = "OmniCure API")
    {
        services.AddEndpointsApiExplorer();
        services.AddSwaggerGen(c =>
        {
            c.SwaggerDoc("v1", new OpenApiInfo { Title = apiName, Version = "v1" });

            c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
            {
                Name = "Authorization",
                Type = SecuritySchemeType.Http,
                Scheme = "bearer",
                BearerFormat = "JWT",
                In = ParameterLocation.Header,
                Description = @"Enter JWT Access Token here to authenticate. <br/>
                        Exp: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            });

            c.AddSecurityRequirement(new OpenApiSecurityRequirement()
            {
                {
                    new OpenApiSecurityScheme
                    {
                        Reference = new OpenApiReference
                        {
                            Type = ReferenceType.SecurityScheme,
                            Id = "Bearer"
                        },
                    },
                    new List<string>()
                }
            });
        });

        return services;
    }
}
