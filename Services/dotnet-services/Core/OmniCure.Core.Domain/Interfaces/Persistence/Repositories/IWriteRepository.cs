using System.Linq.Expressions;

namespace OmniCure.Core.Domain.Interfaces.Persistence.Repositories;

public interface IWriteRepository<T> where T : class
{
    Task<T?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default);
    
    Task<T?> GetByIdAsync<TKey>(TKey id, CancellationToken cancellationToken = default);
    
    Task<T> GetFirstAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default);

    Task<T?> GetFirstOrDefaultAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default);

    Task AddAsync(T entity, CancellationToken cancellationToken = default);

    void Update(T entity);

    void Remove(T entity, bool hardDelete = false);

    Task AddRangeAsync(IEnumerable<T> entities, CancellationToken cancellationToken = default);

    Task<List<T>> GetListAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default);

    Task<List<T>> GetAsync(
        Expression<Func<T, bool>>? filter = null,
        string? sortBy = null,
        string? sortDirection = "asc",
        CancellationToken cancellationToken = default);
    
    IQueryable<T> GetQueryable();
    
    void RemoveRange(IEnumerable<T> entities, bool hardDelete = false);

    Task<bool> ExistsAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default);
}
