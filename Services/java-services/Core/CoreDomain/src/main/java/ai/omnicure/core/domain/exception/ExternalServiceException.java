package ai.omnicure.core.domain.exception;

import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {
    
    private final String serviceName;
    private final int statusCode;

    public ExternalServiceException(String serviceName, String message) {
        super(message);
        this.serviceName = serviceName;
        this.statusCode = 502;
    }

    public ExternalServiceException(String serviceName, String message, int statusCode) {
        super(message);
        this.serviceName = serviceName;
        this.statusCode = statusCode;
    }
}
