using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Domain.Interfaces.Persistence;
using OmniCure.Core.Shared.Pagination;

namespace OmniCure.Core.Infrastructure.Persistence.Repositories;

public class QueryExecutor : IQueryExecutor
{
    public async Task<PagedList<TResult>> ToPagedListAsync<TResult>(
        IQueryable<TResult> query,
        int pageNumber,
        int pageSize,
        CancellationToken cancellationToken = default)
    {
        var totalCount = await query.CountAsync(cancellationToken);

        var items = await query
            .Skip((pageNumber - 1) * pageSize)
            .Take(pageSize)
            .ToListAsync(cancellationToken);

        return new PagedList<TResult>(items, totalCount, pageNumber, pageSize);
    }
}
