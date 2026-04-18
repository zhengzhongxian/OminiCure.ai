package ai.omnicure.core.application.behavior;

import an.awesome.pipelinr.Command;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(1)
@RequiredArgsConstructor
public class ValidationMiddleware implements Command.Middleware {

    private final Validator validator;

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        Set<ConstraintViolation<C>> violations = validator.validate(command);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return next.invoke();
    }
}
