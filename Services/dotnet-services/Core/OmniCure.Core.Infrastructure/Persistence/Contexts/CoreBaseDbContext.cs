using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Domain.Common;
using OmniCure.Core.Application.Common.Interfaces.Services;
using System.Linq.Expressions;

namespace OmniCure.Core.Infrastructure.Persistence.Contexts;

public abstract class CoreBaseDbContext : DbContext
{
    protected readonly ICurrentUserService CurrentUserService;

    protected CoreBaseDbContext(DbContextOptions options, ICurrentUserService currentUserService) : base(options)
    {
        CurrentUserService = currentUserService;
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);
        
        foreach (var entityType in modelBuilder.Model.GetEntityTypes())
        {
            var entityBuilder = modelBuilder.Entity(entityType.ClrType);

            if (typeof(IRowVersion).IsAssignableFrom(entityType.ClrType))
            {
                entityBuilder.Property(nameof(IRowVersion.RowVersion))
                    .HasColumnName("row_version")
                    .IsRowVersion();
            }

            if (typeof(IAuditable).IsAssignableFrom(entityType.ClrType))
            {
                entityBuilder.Property(nameof(IAuditable.CreatedAt)).HasColumnName("created_at");
                entityBuilder.Property(nameof(IAuditable.CreatedBy)).HasColumnName("created_by");
                entityBuilder.Property(nameof(IAuditable.UpdatedAt)).HasColumnName("updated_at");
                entityBuilder.Property(nameof(IAuditable.UpdatedBy)).HasColumnName("updated_by");
            }

            if (typeof(ISoftDelete).IsAssignableFrom(entityType.ClrType))
            {
                entityBuilder.Property(nameof(ISoftDelete.IsDeleted)).HasColumnName("is_deleted");
                entityBuilder.Property(nameof(ISoftDelete.DeletedAt)).HasColumnName("deleted_at");
                entityBuilder.Property(nameof(ISoftDelete.DeletedBy)).HasColumnName("deleted_by");

                entityBuilder.HasQueryFilter(CreateSoftDeleteFilter(entityType.ClrType));
            }
        }
    }

    public override int SaveChanges()
    {
        UpdateAuditFields();
        return base.SaveChanges();
    }

    public override async Task<int> SaveChangesAsync(CancellationToken cancellationToken = default)
    {
        UpdateAuditFields();
        return await base.SaveChangesAsync(cancellationToken);
    }

    private void UpdateAuditFields()
    {
        var userId = CurrentUserService.GetUserId()?.ToString();
        var currentTime = DateTime.UtcNow;

        foreach (var entry in ChangeTracker.Entries<IAuditable>())
        {
            switch (entry.State)
            {
                case EntityState.Added:
                    entry.Entity.CreatedAt = currentTime;
                    entry.Entity.CreatedBy = userId;
                    entry.Entity.UpdatedAt = currentTime;
                    entry.Entity.UpdatedBy = userId;
                    break;

                case EntityState.Modified:
                    entry.Entity.UpdatedAt = currentTime;
                    entry.Entity.UpdatedBy = userId;
                    entry.Property(e => e.CreatedAt).IsModified = false;
                    entry.Property(e => e.CreatedBy).IsModified = false;
                    break;
            }
        }
    }

    private static LambdaExpression CreateSoftDeleteFilter(Type type)
    {
        var parameter = Expression.Parameter(type, "e");
        var property = Expression.Property(parameter, nameof(ISoftDelete.IsDeleted));
        var constant = Expression.Constant(false);
        var equality = Expression.Equal(property, constant);
        return Expression.Lambda(equality, parameter);
    }
}
