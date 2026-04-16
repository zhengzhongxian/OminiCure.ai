using OmniCure.Core.Shared.Constants;

namespace OmniCure.Core.WebShared.Extensions;

public static class AuthorizationExtensions
{
    public static IServiceCollection AddPolicies(this IServiceCollection services)
    {
        services.AddAuthorization(options =>
        {
            var permissions = PermissionConstants.GetAllPermissions();

            foreach (var permission in permissions)
            {
                options.AddPolicy(permission, policy => 
                    policy.RequireClaim("Permission", permission));
            }
        });

        return services;
    }
}
