package ai.omnicure.core.application.cqrs;

import an.awesome.pipelinr.Command.Handler;

public interface Command<R> extends an.awesome.pipelinr.Command<R> {

    interface CommandHandler<C extends Command<R>, R> extends Handler<C, R> {
    }
}
