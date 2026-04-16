namespace OmniCure.Core.Domain.Interfaces.Persistence.Repositories;

public interface IDbTransaction : IDisposable, IAsyncDisposable
{
    Guid TransactionId { get; }
    void Commit();
    Task CommitAsync(CancellationToken cancellationToken = default);
    void Rollback();
    Task RollbackAsync(CancellationToken cancellationToken = default);
}
