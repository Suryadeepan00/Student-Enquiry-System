package com.kmie.demo_booking.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException exception
    ) {
        return error(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateBooking(
            DuplicateBookingException exception
    ) {
        return error(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        fieldErrors.putIfAbsent(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        Map<String, Object> body = errorBody(
                HttpStatus.BAD_REQUEST,
                "Please correct the invalid fields."
        );

        body.put("fieldErrors", fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableBody(
            HttpMessageNotReadableException exception
    ) {
        return error(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or invalid. Check JSON syntax, "
                        + "date/time formats, demoSlot and status values. "
                        + "Dates: yyyy-MM-dd. "
                        + "Date/time: yyyy-MM-dd'T'HH:mm:ss. "
                        + "Slots: SLOT_1_PM, SLOT_3_PM, SLOT_5_PM, SLOT_7_PM. "
                        + "Statuses: BOOKED, ATTENDED, CANCELLED."
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        return error(
                HttpStatus.BAD_REQUEST,
                "Invalid value for parameter '" + exception.getName()
                        + "'. Check the ID, date or booking status format."
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException exception
    ) {
        return error(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataConflict(
            DataIntegrityViolationException exception
    ) {
        log.error("Database integrity constraint violation", exception);

        return error(
                HttpStatus.CONFLICT,
                "The request conflicts with existing data or a database "
                        + "constraint. Check for duplicate student or booking "
                        + "details, then refresh and try again."
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseFailure(
            DataAccessException exception
    ) {
        log.error("Database operation failed", exception);

        return error(
                HttpStatus.SERVICE_UNAVAILABLE,
                "A database operation failed. Please try again later. "
                        + "If you were saving a record, check whether it "
                        + "was saved before resubmitting."
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedError(
            Exception exception
    ) {
        log.error("Unexpected application error", exception);

        return error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later."
        );
    }

    private ResponseEntity<Map<String, Object>> error(
            HttpStatus status,
            String message
    ) {
        return ResponseEntity.status(status)
                .body(errorBody(status, message));
    }

    private Map<String, Object> errorBody(
            HttpStatus status,
            String message
    ) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return body;
    }
}