using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Domain.Interfaces.Persistence.Repositories;

namespace OmniCure.Core.Infrastructure.Persistence.Repositories;

public class RawSqlRepository<TReadDbContext>(TReadDbContext readDbContext) : IRawSqlRepository 
    where TReadDbContext : DbContext
{
    public async Task<List<TResult>> FromSqlInterpolatedAsync<TResult>(
        FormattableString sql,
        CancellationToken cancellationToken = default) where TResult : class
    {
        return await readDbContext.Set<TResult>()
            .FromSqlInterpolated(sql)
            .ToListAsync(cancellationToken);
    }
}
