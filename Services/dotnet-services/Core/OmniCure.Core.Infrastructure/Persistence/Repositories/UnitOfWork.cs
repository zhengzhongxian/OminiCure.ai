using Microsoft.EntityFrameworkCore;
using OmniCure.Core.Domain.Interfaces.Persistence.Repositories;
using IDbTransaction = OmniCure.Core.Domain.Interfaces.Persistence.Repositories.IDbTransaction;

namespace OmniCure.Core.Infrastructure.Persistence.Repositories;

public class UnitOfWork<TWriteDbContext, TReadDbContext>(
    TWriteDbContext writeDbContext, 
    TReadDbContext readDbContext) : IUnitOfWork 
    where TWriteDbContext : DbContext
    where TReadDbContext : DbContext
{
    private Dictionary<string, object>? _readRepositories;
    private Dictionary<string, object>? _writeRepositories;
    private IRawSqlRepository? _queryRepository;
    
    public IReadRepository<T> GetReadRepository<T>() where T : class
    {
        _readRepositories ??= new Dictionary<string, object>();
        var type = typeof(T).Name;

        if (_readRepositories.TryGetValue(type, out var repo))
        {
            return (IReadRepository<T>)repo;
        }

        var repositoryType = typeof(ReadRepository<>);
        var repositoryInstance = Activator.CreateInstance(repositoryType.MakeGenericType(typeof(T)), readDbContext);

        if (repositoryInstance == null)
            throw new InvalidOperationException($"Unable to create read repository for type {type}");
            
        _readRepositories.Add(type, repositoryInstance);
        
        return (IReadRepository<T>)repositoryInstance;
    }

    public IWriteRepository<T> GetWriteRepository<T>() where T : class
    {
        _writeRepositories ??= new Dictionary<string, object>();
        var type = typeof(T).Name;

        if (_writeRepositories.TryGetValue(type, out var repo))
        {
            return (IWriteRepository<T>)repo;
        }

        var repositoryType = typeof(WriteRepository<>);
        var repositoryInstance = Activator.CreateInstance(repositoryType.MakeGenericType(typeof(T)), writeDbContext);

        if (repositoryInstance == null)
            throw new InvalidOperationException($"Unable to create write repository for type {type}");

        _writeRepositories.Add(type, repositoryInstance);
        
        return (IWriteRepository<T>)repositoryInstance;
    }

    public async Task<int> SaveChangesAsync(CancellationToken cancellationToken = default)
    {
        return await writeDbContext.SaveChangesAsync(cancellationToken);
    }

    public IRawSqlRepository GetQueryRepository()
    {
        _queryRepository ??= new RawSqlRepository<TReadDbContext>(readDbContext);
        return _queryRepository;
    }

    public async Task<IDbTransaction> BeginTransactionAsync(CancellationToken cancellationToken = default)
    {
        var efTransaction = await writeDbContext.Database.BeginTransactionAsync(cancellationToken);
        return new DbTransactionAdapter(efTransaction);
    }

    public async Task ExecuteStrategyAsync(Func<CancellationToken, Task> operation,
        CancellationToken cancellationToken = default)
    {
        var strategy = writeDbContext.Database.CreateExecutionStrategy();
        await strategy.ExecuteAsync(() => operation(cancellationToken));
    }

    public async Task<TResult> ExecuteStrategyAsync<TResult>(Func<CancellationToken, Task<TResult>> operation,
        CancellationToken cancellationToken = default)
    {
        var strategy = writeDbContext.Database.CreateExecutionStrategy();
        return await strategy.ExecuteAsync(() => operation(cancellationToken));
    }

    public async Task ExecuteInTransactionAsync(Func<CancellationToken, Task> operation,
        CancellationToken cancellationToken = default)
    {
        var strategy = writeDbContext.Database.CreateExecutionStrategy();
        await strategy.ExecuteAsync(async () =>
        {
            await using var tx = await writeDbContext.Database.BeginTransactionAsync(cancellationToken);
            try
            {
                await operation(cancellationToken);
                await tx.CommitAsync(cancellationToken);
            }
            catch
            {
                await tx.RollbackAsync(cancellationToken);
                throw;
            }
        });
    }

    public async Task<TResult> ExecuteInTransactionAsync<TResult>(Func<CancellationToken, Task<TResult>> operation,
        CancellationToken cancellationToken = default)
    {
        var strategy = writeDbContext.Database.CreateExecutionStrategy();
        return await strategy.ExecuteAsync(async () =>
        {
            await using var tx = await writeDbContext.Database.BeginTransactionAsync(cancellationToken);
            try
            {
                var result = await operation(cancellationToken);
                await tx.CommitAsync(cancellationToken);
                return result;
            }
            catch
            {
                await tx.RollbackAsync(cancellationToken);
                throw;
            }
        });
    }

    public void Dispose()
    {
        writeDbContext.Dispose();
        readDbContext.Dispose();
        GC.SuppressFinalize(this);
    }
}
