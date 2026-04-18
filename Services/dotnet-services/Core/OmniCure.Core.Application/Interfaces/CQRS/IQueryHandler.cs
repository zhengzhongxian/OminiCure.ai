using MediatR;

namespace OmniCure.Core.Application.Interfaces.CQRS;

public interface IQueryHandler<in TQuery, TResponse> : IRequestHandler<TQuery, TResponse>
    where TQuery : IQuery<TResponse>;
