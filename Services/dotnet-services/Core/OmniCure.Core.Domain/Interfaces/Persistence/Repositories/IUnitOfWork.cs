namespace OmniCure.Core.Domain.Interfaces.Persistence.Repositories;

public interface IUnitOfWork : IDisposable
{
    IReadRepository<T> GetReadRepository<T>() where T : class;
    
    IWriteRepository<T> GetWriteRepository<T>() where T : class;
    
    IRawSqlRepository GetQueryRepository();
    
    Task<int> SaveChangesAsync(CancellationToken cancellationToken = default);
    
    Task<IDbTransaction> BeginTransactionAsync(CancellationToken cancellationToken = default);
    
    Task ExecuteStrategyAsync(Func<CancellationToken, Task> operation, CancellationToken cancellationToken = default);
    
    Task<TResult> ExecuteStrategyAsync<TResult>(Func<CancellationToken, Task<TResult>> operation, CancellationToken cancellationToken = default);
    
    Task ExecuteInTransactionAsync(Func<CancellationToken, Task> operation, CancellationToken cancellationToken = default);
    
    Task<TResult> ExecuteInTransactionAsync<TResult>(Func<CancellationToken, Task<TResult>> operation, CancellationToken cancellationToken = default);
}
