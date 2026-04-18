namespace OmniCure.Core.Application.Common.Interfaces.Services;

public interface ICurrentUserService
{
    Guid? GetUserId();
}
