package ai.omnicure.core.application.cqrs;

import an.awesome.pipelinr.Command.Handler;
import an.awesome.pipelinr.Voidy;

public interface VoidCommand extends an.awesome.pipelinr.Command<Voidy> {

    interface VoidCommandHandler<C extends VoidCommand> extends Handler<C, Voidy> {
    }
}
