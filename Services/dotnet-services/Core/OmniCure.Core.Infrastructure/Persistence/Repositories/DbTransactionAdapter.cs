using System.Data;
using Microsoft.EntityFrameworkCore.Storage;
using OmniCure.Core.Domain.Interfaces.Persistence.Repositories;
using IDbTransaction = OmniCure.Core.Domain.Interfaces.Persistence.Repositories.IDbTransaction;

namespace OmniCure.Core.Infrastructure.Persistence.Repositories;

public class DbTransactionAdapter(IDbContextTransaction efTransaction) : IDbTransaction
{
    public Guid TransactionId => efTransaction.TransactionId;

    public void Commit() => efTransaction.Commit();

    public async Task CommitAsync(CancellationToken cancellationToken = default) =>
        await efTransaction.CommitAsync(cancellationToken);

    public void Rollback() => efTransaction.Rollback();

    public async Task RollbackAsync(CancellationToken cancellationToken = default) =>
        await efTransaction.RollbackAsync(cancellationToken);

    public void Dispose() => efTransaction.Dispose();

    public async ValueTask DisposeAsync() =>
        await efTransaction.DisposeAsync();

    internal System.Data.IDbTransaction GetDbTransaction() =>
        efTransaction.GetDbTransaction();
        
    internal System.Data.IDbConnection GetDbConnection() => 
        efTransaction.GetDbTransaction().Connection!;
}
