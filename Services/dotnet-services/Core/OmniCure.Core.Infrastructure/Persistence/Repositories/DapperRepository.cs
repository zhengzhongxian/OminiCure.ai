using Dapper;
using OmniCure.Core.Domain.Interfaces.Persistence;
using OmniCure.Core.Domain.Interfaces.Persistence.Repositories;
using IDbTransaction = OmniCure.Core.Domain.Interfaces.Persistence.Repositories.IDbTransaction;

namespace OmniCure.Core.Infrastructure.Persistence.Repositories;

public class DapperRepository(IDbConnectionFactory connectionFactory) : IDapperRepository
{
    private static System.Data.IDbTransaction? TryGetRealTransaction(IDbTransaction? transaction)
    {
        if (transaction is DbTransactionAdapter adapter)
        {
            return adapter.GetDbTransaction();
        }
        return null;
    }

    public async Task<IReadOnlyList<T>> QueryAsync<T>(string sql, object? param = null, IDbTransaction? transaction = null, CancellationToken cancellationToken = default)
    {
        var realTx = TryGetRealTransaction(transaction);

        using var connection = realTx?.Connection ?? connectionFactory.CreateReadConnection();
        if (realTx == null) connection.Open();

        var def = new CommandDefinition(sql, param, realTx, cancellationToken: cancellationToken);
        var result = await connection.QueryAsync<T>(def);
        return result.AsList();
    }

    public async Task<T?> QueryFirstOrDefaultAsync<T>(string sql, object? param = null, IDbTransaction? transaction = null, CancellationToken cancellationToken = default)
    {
        var realTx = TryGetRealTransaction(transaction);
        using var connection = realTx?.Connection ?? connectionFactory.CreateReadConnection();

        if (realTx == null) connection.Open();

        var def = new CommandDefinition(sql, param, realTx, cancellationToken: cancellationToken);
        return await connection.QueryFirstOrDefaultAsync<T>(def);
    }

    public async Task<T> QuerySingleAsync<T>(string sql, object? param = null, IDbTransaction? transaction = null, CancellationToken cancellationToken = default)
    {
        var realTx = TryGetRealTransaction(transaction);
        using var connection = realTx?.Connection ?? connectionFactory.CreateReadConnection();

        if (realTx == null) connection.Open();

        var def = new CommandDefinition(sql, param, realTx, cancellationToken: cancellationToken);
        return await connection.QuerySingleAsync<T>(def);
    }

    public async Task<int> ExecuteAsync(string sql, object? param = null, IDbTransaction? transaction = null, CancellationToken cancellationToken = default)
    {
        var realTx = TryGetRealTransaction(transaction);
        using var connection = realTx?.Connection ?? connectionFactory.CreateReadConnection();

        if (realTx == null) connection.Open();

        var def = new CommandDefinition(sql, param, realTx, cancellationToken: cancellationToken);
        return await connection.ExecuteAsync(def);
    }

    public async Task<T> ExecuteScalarAsync<T>(string sql, object? param = null, IDbTransaction? transaction = null, CancellationToken cancellationToken = default)
    {
        var realTx = TryGetRealTransaction(transaction);
        using var connection = realTx?.Connection ?? connectionFactory.CreateReadConnection();

        if (realTx == null) connection.Open();

        var def = new CommandDefinition(sql, param, realTx, cancellationToken: cancellationToken);
        return (await connection.ExecuteScalarAsync<T>(def))!;
    }
}
