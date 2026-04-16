using System.Linq.Expressions;
using OmniCure.Core.Shared.Pagination;

namespace OmniCure.Core.Domain.Interfaces.Persistence.Repositories;

public interface IReadRepository<T> where T : class
{
    IQueryable<T> GetQueryable();
    
    IQueryable<T> GetQueryable(
        Expression<Func<T, bool>>? filter,
        string includeProperties = "");

    Task<T?> GetByIdAsync(Guid id, CancellationToken cancellationToken = default);
    
    Task<T?> GetByIdAsync<TKey>(TKey id, CancellationToken cancellationToken = default);

    Task<List<T>> FindAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default);

    Task<PagedList<T>> GetPagedAsync(
        PagedParams pagedParams,
        Expression<Func<T, bool>>? filter = null,
        string includeProperties = "",
        CancellationToken cancellationToken = default);

    Task<T?> GetFirstOrDefaultAsync(
        Expression<Func<T, bool>> filter, string includeProperties = "",
        CancellationToken cancellationToken = default);

    Task<T> GetFirstAsync(Expression<Func<T, bool>> filter, string includeProperties = "", CancellationToken cancellationToken = default);

    Task<List<T>> GetAsync(
        Expression<Func<T, bool>>? filter = null,
        string? sortBy = null, string? sortDirection = "asc",
        string includeProperties = "",
        CancellationToken cancellationToken = default);

    Task<bool> ExistsAsync(Expression<Func<T, bool>> predicate, CancellationToken cancellationToken = default);

    Task<int> CountAsync(Expression<Func<T, bool>>? predicate = null, CancellationToken cancellationToken = default);
}
