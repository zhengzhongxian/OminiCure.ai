using MediatR;

namespace OmniCure.Core.Application.Interfaces.CQRS;

public interface ICommand<out TResponse> : IRequest<TResponse>;

public interface ICommand : IRequest;
