package ai.omnicure.core.web.exception;

import ai.omnicure.core.domain.exception.DomainException;
import ai.omnicure.core.domain.exception.ExternalServiceException;
import ai.omnicure.core.shared.constant.message.infra.DatabaseErrorMessage;
import ai.omnicure.core.shared.constant.message.infra.SystemErrorMessage;
import ai.omnicure.core.web.model.response.v1.ResultRes;
import org.apache.catalina.connector.ClientAbortException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultRes<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        logger.warn("Validation Error: {}", String.join(", ", errors));

        ResultRes<Object> response = ResultRes.errorResult(
                SystemErrorMessage.Request.INVALID_INPUT,
                HttpStatus.BAD_REQUEST.value(),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({AsyncRequestTimeoutException.class, ClientAbortException.class})
    public ResponseEntity<ResultRes<Object>> handleOperationCanceledException(Exception ex) {
        logger.warn("Client request was cancelled.");
        ResultRes<Object> response = ResultRes.errorResult(
                SystemErrorMessage.Request.CANCELLED,
                499,
                null
        );
        return ResponseEntity.status(499).body(response);
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ResultRes<Object>> handleTimeoutException(QueryTimeoutException ex) {
        logger.error("Database connection timed out.");
        ResultRes<Object> response = ResultRes.errorResult(
                DatabaseErrorMessage.TIMEOUT,
                HttpStatus.GATEWAY_TIMEOUT.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(response);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ResultRes<Object>> handleInvalidOperationException(RuntimeException ex) {
        logger.warn("Invalid Operation: {}", ex.getMessage());
        ResultRes<Object> response = ResultRes.errorResult(
                SystemErrorMessage.Request.INVALID_OPERATION,
                HttpStatus.BAD_REQUEST.value(),
                Collections.singletonList(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ResultRes<Object>> handleDbUpdateConcurrencyException(OptimisticLockingFailureException ex) {
        logger.warn("Database concurrency conflict.");
        ResultRes<Object> response = ResultRes.errorResult(
                DatabaseErrorMessage.CONCURRENCY_CONFLICT,
                HttpStatus.CONFLICT.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResultRes<Object>> handleDatabaseException(DataAccessException dbEx) {
        Throwable rootCause = dbEx.getRootCause();
        if (!(rootCause instanceof PSQLException sqlEx)) {
            logger.error("Generic Database Update Error: {}", dbEx.getMessage());
            ResultRes<Object> response = ResultRes.errorResult(
                    DatabaseErrorMessage.UPDATE_ERROR,
                    HttpStatus.BAD_REQUEST.value(),
                    Collections.singletonList(dbEx.getMessage())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String sqlState = sqlEx.getSQLState();
        int statusCode;
        String message = switch (sqlState != null ? sqlState : "") {
            case "23505" -> {
                statusCode = HttpStatus.CONFLICT.value();
                yield DatabaseErrorMessage.DUPLICATE;
            }
            case "23503" -> {
                statusCode = HttpStatus.BAD_REQUEST.value();
                yield DatabaseErrorMessage.FOREIGN_KEY_VIOLATION;
            }
            case "22001" -> {
                statusCode = HttpStatus.BAD_REQUEST.value();
                yield DatabaseErrorMessage.DATA_TOO_LONG;
            }
            case "23502" -> {
                statusCode = HttpStatus.BAD_REQUEST.value();
                yield DatabaseErrorMessage.NOT_NULL_VIOLATION;
            }
            case "22P02" -> {
                statusCode = HttpStatus.BAD_REQUEST.value();
                yield DatabaseErrorMessage.DATA_TYPE_CONVERSION;
            }
            case "40P01" -> {
                statusCode = HttpStatus.CONFLICT.value();
                yield DatabaseErrorMessage.DEADLOCK;
            }
            case "53300" -> {
                statusCode = HttpStatus.TOO_MANY_REQUESTS.value();
                yield DatabaseErrorMessage.THROTTLED;
            }
            case "08001", "08006" -> {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
                yield DatabaseErrorMessage.CONNECTION_FAILED;
            }
            case "28P01" -> {
                statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
                yield DatabaseErrorMessage.LOGIN_FAILED;
            }
            default -> {
                statusCode = HttpStatus.BAD_REQUEST.value();
                yield DatabaseErrorMessage.GENERAL;
            }
        };

        logger.error("SQL Error Code [{}]: {}", sqlState, sqlEx.getMessage());
        ResultRes<Object> response = ResultRes.errorResult(
                message,
                statusCode,
                Collections.singletonList(sqlEx.getMessage())
        );
        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ResultRes<Object>> handleDomainException(DomainException domainEx) {
        logger.warn("Business Rule Violation: {}", domainEx.getMessage());
        ResultRes<Object> response = ResultRes.errorResult(
                domainEx.getMessage(),
                domainEx.getStatusCode(),
                null
        );
        return ResponseEntity.status(domainEx.getStatusCode()).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ResultRes<Object>> handleExternalServiceException(ExternalServiceException extEx) {
        logger.error("External Service Failure [{}]: {}", extEx.getServiceName(), extEx.getMessage());
        String msg = String.format(SystemErrorMessage.External.SERVICE_UNAVAILABLE, extEx.getServiceName());
        ResultRes<Object> response = ResultRes.errorResult(
                msg,
                extEx.getStatusCode(),
                null
        );
        return ResponseEntity.status(extEx.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultRes<Object>> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        ResultRes<Object> response = ResultRes.errorResult(
                SystemErrorMessage.UNHANDLED_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Collections.singletonList(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
