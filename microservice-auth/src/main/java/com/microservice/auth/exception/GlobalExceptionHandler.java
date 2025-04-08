package com.microservice.auth.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + ": " + errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation error")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        log.error("Validation error: {}", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid username or password")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(List.of("Authentication failed: " + ex.getMessage()))
                .build();

        log.error("Authentication failed: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication failed: " + ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(List.of(ex.getMessage()))
                .build();

        log.error("Authentication error: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(List.of("Unexpected error: " + ex.getMessage()))
                .build();

        log.error("Unexpected error: ", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
