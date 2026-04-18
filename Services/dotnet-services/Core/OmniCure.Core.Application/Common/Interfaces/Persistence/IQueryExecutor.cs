using OmniCure.Core.Shared.Pagination;

namespace OmniCure.Core.Application.Common.Interfaces.Persistence;

public interface IQueryExecutor
{
    Task<PagedList<TResult>> ToPagedListAsync<TResult>(
        IQueryable<TResult> query,
        int pageNumber,
        int pageSize,
        CancellationToken cancellationToken = default);
}
