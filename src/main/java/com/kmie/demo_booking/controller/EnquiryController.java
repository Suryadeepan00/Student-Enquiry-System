package com.kmie.demo_booking.controller;

import com.kmie.demo_booking.dto.EnquiryRequest;
import com.kmie.demo_booking.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEnquiry(
            @Valid @RequestBody EnquiryRequest request
    ) {
        Map<String, Object> response =
                enquiryService.createEnquiry(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}