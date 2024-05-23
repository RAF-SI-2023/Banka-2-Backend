package rs.edu.raf.IAMService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleUserNotFound(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ExceptionResponse> handleBadCredentials(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, status);
    }
}