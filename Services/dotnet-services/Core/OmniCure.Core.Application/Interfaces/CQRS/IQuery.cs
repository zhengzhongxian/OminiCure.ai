using MediatR;

namespace OmniCure.Core.Application.Interfaces.CQRS;

public interface IQuery<out TResponse> : IRequest<TResponse>;
