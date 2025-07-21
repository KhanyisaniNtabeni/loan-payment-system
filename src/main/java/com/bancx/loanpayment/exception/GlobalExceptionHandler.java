package com.bancx.loanpayment.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler that captures exceptions thrown in
 * controllers and returns uniform, meaningful error responses.
 *
 * Ensures all exceptions are logged and appropriate HTTP statuses
 * with detailed messages are sent back to clients.
 *
 * Handles:
 * - Not Found scenarios (LoanNotFoundException)
 * - Validation errors
 * - Business logic errors (IllegalStateException, IllegalArgumentException)
 * - Optimistic locking conflicts
 * - Generic uncaught exceptions
 *
 * Uses a consistent {@link ErrorResponse} DTO for all error responses.
 *
 * Author: Khanyisani Luyanda Ntabeni
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles LoanNotFoundException thrown when a loan cannot be found.
     * Returns 404 NOT FOUND with error message.
     *
     * @param ex      the LoanNotFoundException exception
     * @param request the HttpServletRequest for extracting request URI
     * @return ResponseEntity with {@link ErrorResponse} and HTTP status 404
     */
    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLoanNotFound(LoanNotFoundException ex, HttpServletRequest request) {
        log.warn("Loan not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles IllegalArgumentException, commonly thrown for invalid parameters.
     * Returns 400 BAD REQUEST with descriptive message.
     *
     * @param ex      the IllegalArgumentException exception
     * @param request the HttpServletRequest for extracting request URI
     * @return ResponseEntity with {@link ErrorResponse} and HTTP status 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles optimistic locking exceptions that occur due to concurrent
     * updates. Returns 409 CONFLICT with a standard conflict message.
     *
     * @param ex      the ObjectOptimisticLockingFailureException exception
     * @param request the HttpServletRequest for extracting request URI
     * @return ResponseEntity with {@link ErrorResponse} and HTTP status 409
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(ObjectOptimisticLockingFailureException ex, HttpServletRequest request) {
        log.error("Optimistic locking failure detected", ex);
        String message = "Conflict detected: The loan was updated by another transaction.";
        return buildResponse(HttpStatus.CONFLICT, message, request);
    }

    /**
     * Handles IllegalStateException, typically thrown for business rule violations
     * such as attempting to pay an already settled loan.
     * Returns 409 CONFLICT with error message.
     *
     * @param ex      the IllegalStateException exception
     * @param request the HttpServletRequest for extracting request URI
     * @return ResponseEntity with {@link ErrorResponse} and HTTP status 409
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        log.warn("Illegal state: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles validation exceptions thrown by Spring when @Valid
     * annotated request bodies fail validation.
     * Returns 400 BAD REQUEST with concatenated field error messages.
     *
     * @param ex      the MethodArgumentNotValidException exception
     * @param request the HttpServletRequest for extracting request URI
     * @return ResponseEntity with {@link ErrorResponse} and HTTP status 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder errorMessages = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessages.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ")
        );
        log.warn("Validation errors: {}", errorMessages.toString().trim());
        return buildResponse(HttpStatus.BAD_REQUEST, errorMessages.toString().trim(), request);
    }

    /**
     * Generic fallback exception handler that catches any uncaught exceptions.
     * Returns 500 INTERNAL SERVER ERROR with a generic message.
     *
     * @param ex      the Exception
     * @param request the HttpServletRequest for extracting request URI
     * @return ResponseEntity with {@link ErrorResponse} and HTTP status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    /**
     * Helper method to build the uniform error response entity.
     *
     * @param status  the HTTP status to return
     * @param message the error message to include
     * @param request the HTTP request to extract URI path
     * @return ResponseEntity with populated {@link ErrorResponse} and status
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, status);
    }

    /**
     * DTO representing the error response sent back to clients.
     */
    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String timestamp;
        private int status;
        private String message;
        private String path;
    }
}
