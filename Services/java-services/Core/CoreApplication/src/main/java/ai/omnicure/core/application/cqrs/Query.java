package ai.omnicure.core.application.cqrs;

import an.awesome.pipelinr.Command.Handler;

public interface Query<R> extends an.awesome.pipelinr.Command<R> {

    interface QueryHandler<Q extends Query<R>, R> extends Handler<Q, R> {
    }
}
