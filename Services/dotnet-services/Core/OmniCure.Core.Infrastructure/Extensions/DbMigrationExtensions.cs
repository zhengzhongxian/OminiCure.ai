using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using OmniCure.Core.Shared.Constants;

namespace OmniCure.Core.Infrastructure.Extensions;

public static class DbMigrationExtensions
{
    public static async Task ApplyMigrationsAsync<TWriteDbContext, TReadDbContext>(this IServiceProvider serviceProvider)
        where TWriteDbContext : DbContext
        where TReadDbContext : DbContext
    {
        using var scope = serviceProvider.CreateScope();
        var services = scope.ServiceProvider;
        
        var loggerFactory = services.GetRequiredService<ILoggerFactory>();
        var logger = loggerFactory.CreateLogger(typeof(DbMigrationExtensions));
        var configuration = services.GetRequiredService<IConfiguration>();
        
        var retryCount = 0;
        var maxRetries = configuration.GetValue<int>(KeyConstants.DatabaseInitializationSettings.MaxRetries, 10);
        var baseDelay = configuration.GetValue<int>(KeyConstants.DatabaseInitializationSettings.RetryInterval, 3);

        while (retryCount < maxRetries)
        {
            try
            {
                logger.LogInformation("Applying database migrations... (Attempt {RetryCount}/{MaxRetries})", retryCount + 1, maxRetries);

                var writeDbContext = services.GetRequiredService<TWriteDbContext>();
                await writeDbContext.Database.MigrateAsync();
                logger.LogInformation("WriteDB Migrations applied.");

                var readDbContext = services.GetRequiredService<TReadDbContext>();
                await readDbContext.Database.MigrateAsync();
                logger.LogInformation("ReadDB Migrations applied.");

                logger.LogInformation("[Migration Progress: 100%] Database migrations applied successfully.");
                return;
            }
            catch (Exception ex)
            {
                retryCount++;
                if (retryCount >= maxRetries)
                {
                    logger.LogError(ex, "Database migration failed after {MaxRetries} retries. Aborting.", maxRetries);
                    throw;
                }

                var delay = Math.Min(baseDelay * (int)Math.Pow(2, retryCount - 1), 60);
                logger.LogWarning("Database migration failed (attempt {RetryCount}/{MaxRetries}). Retrying in {Delay}s... Error: {ErrorMessage}", retryCount, maxRetries, delay, ex.Message);
                await Task.Delay(delay * 1000);
            }
        }
    }
}
