package com.lhind.annualleave.http.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<?> handleExceptions(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new RestErrorResponse(LocalDateTime.now(), e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    ResponseEntity<?> handleUserNotAuthenticatedException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new RestErrorResponse(LocalDateTime.now(), e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    private static class RestErrorResponse {
        private final LocalDateTime timestamp;
        private final String message;

        public RestErrorResponse(LocalDateTime timestamp, String message) {
            this.timestamp = timestamp;
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getMessage() {
            return message;
        }
    }
}
