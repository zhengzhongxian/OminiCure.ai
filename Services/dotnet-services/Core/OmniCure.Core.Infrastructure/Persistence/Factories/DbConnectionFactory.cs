using System.Data;
using OmniCure.Core.Domain.Interfaces.Persistence;
// Nếu dùng SQL Server hãy đổi thành: using Microsoft.Data.SqlClient;
using Npgsql; 

namespace OmniCure.Core.Infrastructure.Persistence.Factories;

public class DbConnectionFactory : IDbConnectionFactory
{
    private readonly string _readConnectionString;

    public DbConnectionFactory(string readConnectionString)
    {
        _readConnectionString = readConnectionString 
            ?? throw new ArgumentNullException(nameof(readConnectionString));
    }

    public IDbConnection CreateReadConnection()
    {
        return new NpgsqlConnection(_readConnectionString);
    }
}
