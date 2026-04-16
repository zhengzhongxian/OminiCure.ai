package ai.omnicure.core.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    
    private final int statusCode;

    public DomainException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public DomainException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
