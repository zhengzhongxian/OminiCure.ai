namespace OmniCure.Core.Domain.Interfaces.Persistence.Repositories;

public interface IRawSqlRepository
{
    Task<List<TResult>> FromSqlInterpolatedAsync<TResult>(
        FormattableString sql,
        CancellationToken cancellationToken = default) where TResult : class;
}
