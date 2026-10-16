package com.kmie.demo_booking.controller;

import com.kmie.demo_booking.dto.BookingRequest;
import com.kmie.demo_booking.dto.StatusUpdateRequest;
import com.kmie.demo_booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(
            @Valid @RequestBody BookingRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.createBooking(request));
    }

    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable("bookingId") Long bookingId,
            @Valid @RequestBody StatusUpdateRequest request
    ) {
        return ResponseEntity.ok(
                bookingService.updateStatus(bookingId, request)
        );
    }
}