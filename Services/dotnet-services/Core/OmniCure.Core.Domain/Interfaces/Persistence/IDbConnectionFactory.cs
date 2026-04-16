using System.Data;

namespace OmniCure.Core.Domain.Interfaces.Persistence;

public interface IDbConnectionFactory
{
    IDbConnection CreateReadConnection();
}
