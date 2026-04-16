using System.Linq.Expressions;
using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Domain.Common;
using OmniCure.Core.Domain.Interfaces.Persistence.Repositories;
using OmniCure.Core.Shared.Pagination;

namespace OmniCure.Core.Infrastructure.Persistence.Repositories;

public class ReadRepository<T>(DbContext readDbContext) : IReadRepository<T> where T : class
{
    public IQueryable<T> GetQueryable()
    {
        IQueryable<T> query = readDbContext.Set<T>();

        if (typeof(IAuditable).IsAssignableFrom(typeof(T)))
        {
            query = query.Cast<IAuditable>()
                .OrderByDescending(e => e.UpdatedAt)
                .Cast<T>();
        }

        return query.AsQueryable();
    }
    
    public IQueryable<T> GetQueryable(
        Expression<Func<T, bool>>? filter,
        string includeProperties = "")
    {
        IQueryable<T> query = readDbContext.Set<T>();

        if (filter != null)
        {
            query = query.Where(filter);
        }

        query = includeProperties
            .Split([','], StringSplitOptions.RemoveEmptyEntries)
            .Aggregate(query, (current, includeProperty) => current.Include(includeProperty.Trim()));

        return query.AsQueryable();
    }

    public async Task<T?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default)
    {
        return await readDbContext.Set<T>().FindAsync([id], cancellationToken);
    }
    
    public async Task<T?> GetByIdAsync<TKey>(TKey id, CancellationToken cancellationToken = default)
    {
        return await readDbContext.Set<T>().FindAsync([id], cancellationToken);
    }
    
    public async Task<List<T>> FindAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default)
    {
        return await GetQueryable().Where(predicate).ToListAsync(cancellationToken);
    }

    public async Task<PagedList<T>> GetPagedAsync(
        PagedParams pagedParams,
        Expression<Func<T, bool>>? filter = null,
        string includeProperties = "",
        CancellationToken cancellationToken = default)
    {
        IQueryable<T> query = readDbContext.Set<T>();

        query = includeProperties
            .Split([','], StringSplitOptions.RemoveEmptyEntries)
            .Aggregate(query, (current, includeProperty) => current.Include(includeProperty));

        if (filter != null)
        {
            query = query.Where(filter);
        }
        
        var count = await query.CountAsync(cancellationToken);
        
        if (!string.IsNullOrWhiteSpace(pagedParams.SortBy))
        {
            query = pagedParams.SortDirection?.ToLower() == "desc"
                ? query.OrderByDescending(v => EF.Property<object>(v, pagedParams.SortBy))
                : query.OrderBy(v => EF.Property<object>(v, pagedParams.SortBy));
        }
        else if (typeof(IAuditable).IsAssignableFrom(typeof(T)))
        {
            query = query.Cast<IAuditable>().OrderByDescending(e => e.UpdatedAt).Cast<T>();
        }

        var items = await query
            .Skip((pagedParams.PageNumber - 1) * pagedParams.PageSize)
            .Take(pagedParams.PageSize)
            .ToListAsync(cancellationToken);

        return new PagedList<T>(items, count, pagedParams.PageNumber, pagedParams.PageSize);
    }

    public async Task<T?> GetFirstOrDefaultAsync(
        Expression<Func<T, bool>> filter, string includeProperties = "",
        CancellationToken cancellationToken = default)
    {
        IQueryable<T> query = readDbContext.Set<T>();
        query = includeProperties
            .Split([','], StringSplitOptions.RemoveEmptyEntries)
            .Aggregate(query, (current, includeProperty) => current.Include(includeProperty));

        return await query.FirstOrDefaultAsync(filter, cancellationToken);
    }

    public async Task<T> GetFirstAsync(Expression<Func<T, bool>> filter, string includeProperties = "", CancellationToken cancellationToken = default)
    {
        IQueryable<T> query = readDbContext.Set<T>();
        query = includeProperties
            .Split([','], StringSplitOptions.RemoveEmptyEntries)
            .Aggregate(query, (current, includeProperty) => current.Include(includeProperty));

        return await query.FirstAsync(filter, cancellationToken);
    }

    public async Task<List<T>> GetAsync(
        Expression<Func<T, bool>>? filter = null,
        string? sortBy = null, string? sortDirection = "asc",
        string includeProperties = "",
        CancellationToken  cancellationToken = default)
    {
        IQueryable<T> query = readDbContext.Set<T>();

        if (filter != null)
        {
            query = query.Where(filter);
        }

        query = includeProperties
            .Split([','], StringSplitOptions.RemoveEmptyEntries)
            .Aggregate(query, (current, includeProperty) => current.Include(includeProperty));

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

    public async Task<bool> ExistsAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default)
    {
        return await readDbContext.Set<T>().AnyAsync(predicate, cancellationToken);
    }

    public async Task<int> CountAsync(Expression<Func<T, bool>>? predicate = null, CancellationToken cancellationToken = default)
    {
        if (predicate == null)
        {
            return await readDbContext.Set<T>().CountAsync(cancellationToken);
        }

        return await readDbContext.Set<T>().CountAsync(predicate, cancellationToken);
    }
}
