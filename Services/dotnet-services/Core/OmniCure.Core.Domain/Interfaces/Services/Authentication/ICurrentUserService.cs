namespace OmniCure.Core.Domain.Interfaces.Services.Authentication;

public interface ICurrentUserService
{
    Guid? GetUserId();
}
