package br.com.autoflex.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //ERROR 400
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorApi> handleBusiness(BusinessException ex, HttpServletRequest request) {
        ErrorApi body = new ErrorApi(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    //ERROR 400 - Para erros de validações (Beans de validações)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErrorApi.FieldViolation> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toViolation)
                .toList();

        ErrorApi body = new ErrorApi(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation error",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.badRequest().body(body);
    }

    //ERRO 401
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorApi> handle401(AuthenticationException ex, HttpServletRequest request) {

        ErrorApi body = new ErrorApi(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Unauthorized - authentication required",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    //ERROR 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorApi> handle403(AccessDeniedException ex, HttpServletRequest request) {

        ErrorApi body = new ErrorApi(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Forbidden - access denied",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    //ERROR 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorApi> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorApi body = new ErrorApi(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    //ERROR 409
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorApi> handleConflict(ConflictException ex, HttpServletRequest request) {
        ErrorApi body = new ErrorApi(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //ERROR 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorApi body = new ErrorApi(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Unexpected error",
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ErrorApi.FieldViolation toViolation(FieldError fieldError) {
        return new ErrorApi.FieldViolation(fieldError.getField(), fieldError.getDefaultMessage());
    }

}