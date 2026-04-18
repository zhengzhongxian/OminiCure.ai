package ai.omnicure.core.application.config;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

@Configuration
public class PipelinRConfig {

    @Bean
    public Pipeline pipeline(
            ObjectProvider<Command.Handler> handlers,
            ObjectProvider<Command.Middleware> middlewares) {
        return new Pipelinr()
                .with(() -> handlers.stream())
                .with(() -> middlewares.orderedStream());
    }
}
