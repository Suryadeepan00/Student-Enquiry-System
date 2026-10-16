package com.kmie.demo_booking.exception;

public class DuplicateBookingException extends RuntimeException {

    public DuplicateBookingException(String message) {
        super(message);
    }
}