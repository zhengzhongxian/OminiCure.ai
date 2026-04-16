namespace OmniCure.Core.Domain.Common;

public interface IRowVersion
{
    byte[]? RowVersion { get; set; }
}
