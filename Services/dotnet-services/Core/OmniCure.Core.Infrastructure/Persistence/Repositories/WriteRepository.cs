using System.Linq.Expressions;
using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Domain.Common;
using OmniCure.Core.Domain.Interfaces.Persistence.Repositories;

namespace OmniCure.Core.Infrastructure.Persistence.Repositories;

public class WriteRepository<T>(DbContext writeDbContext) : IWriteRepository<T> where T : class
{
    public async Task<T?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        return await writeDbContext.Set<T>().FindAsync([id], cancellationToken);
    }
    
    public async Task<T?> GetByIdAsync<TKey>(TKey id, CancellationToken cancellationToken = default)
    {
        return await writeDbContext.Set<T>().FindAsync([id], cancellationToken);
    }
    
    public async Task<T> GetFirstAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default)
    {
        return await writeDbContext.Set<T>().FirstAsync(predicate, cancellationToken);
    }

    public async Task<T?> GetFirstOrDefaultAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default)
    {
        return await writeDbContext.Set<T>().FirstOrDefaultAsync(predicate, cancellationToken);
    }

    public async Task AddAsync(T entity, CancellationToken cancellationToken = default)
    {
        await writeDbContext.Set<T>().AddAsync(entity, cancellationToken);
    }

    public void Update(T entity)
    {
        writeDbContext.Set<T>().Update(entity);
    }

    public void Remove(T entity, bool hardDelete = false)
    {
        if (hardDelete)
        {
            writeDbContext.Set<T>().Remove(entity);
            return;
        }

        if (entity is ISoftDelete softDelete)
        {
            softDelete.IsDeleted = true;
            writeDbContext.Entry(softDelete).State = EntityState.Modified;
        }
        else
        {
            writeDbContext.Set<T>().Remove(entity);
        }
    }

    public async Task AddRangeAsync(IEnumerable<T> entities, CancellationToken cancellationToken = default)
    {
        await writeDbContext.Set<T>().AddRangeAsync(entities, cancellationToken);
    }

    public async Task<List<T>> GetListAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default)
    {
        return await writeDbContext.Set<T>()
            .Where(predicate)
            .ToListAsync(cancellationToken);
    }

    public async Task<List<T>> GetAsync(
        Expression<Func<T, bool>>? filter = null,
        string? sortBy = null,
        string? sortDirection = "asc",
        CancellationToken cancellationToken = default)
    {
        IQueryable<T> query = writeDbContext.Set<T>();

        if (filter != null)
        {
            query = query.Where(filter);
        }

        if (!string.IsNullOrWhiteSpace(sortBy))
        {
            query = sortDirection?.ToLower() == "desc"
                ? query.OrderByDescending(v => EF.Property<object>(v, sortBy))
                : query.OrderBy(v => EF.Property<object>(v, sortBy));
        }
        else if (typeof(IAuditable).IsAssignableFrom(typeof(T)))
        {
            query = query.Cast<IAuditable>().OrderByDescending(e => e.UpdatedAt).Cast<T>();
        }

        return await query.ToListAsync(cancellationToken);
    }
    
    public IQueryable<T> GetQueryable()
    {
        return writeDbContext.Set<T>().AsQueryable();
    }
    
    public void RemoveRange(IEnumerable<T> entities, bool hardDelete = false)
    {
        if (hardDelete)
        {
            writeDbContext.Set<T>().RemoveRange(entities);
            return;
        }

        foreach (var entity in entities)
        {
            if (entity is ISoftDelete softDelete)
            {
                softDelete.IsDeleted = true;
                writeDbContext.Entry(softDelete).State = EntityState.Modified;
            }
            else
            {
                writeDbContext.Set<T>().Remove(entity);
            }
        }
    }

    public async Task<bool> ExistsAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default)
    {
        return await writeDbContext.Set<T>().AnyAsync(predicate, cancellationToken);
    }
}
