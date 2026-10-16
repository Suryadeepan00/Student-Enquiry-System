package com.kmie.demo_booking.controller;

import com.kmie.demo_booking.entity.BookingStatus;
import com.kmie.demo_booking.service.CounsellorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CounsellorController {

    private final CounsellorService counsellorService;

    public CounsellorController(CounsellorService counsellorService) {
        this.counsellorService = counsellorService;
    }

    @GetMapping("/enquiries")
    public List<Map<String, Object>> getEnquiries(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return counsellorService.getEnquiries(date);
    }

    @GetMapping("/bookings")
    public List<Map<String, Object>> getBookings(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam(name = "status", required = false)
            BookingStatus status
    ) {
        return counsellorService.getBookings(date, status);
    }

    @GetMapping("/dashboard")
    public Map<String, Long> getDashboard() {
        return counsellorService.getDashboard();
    }
}