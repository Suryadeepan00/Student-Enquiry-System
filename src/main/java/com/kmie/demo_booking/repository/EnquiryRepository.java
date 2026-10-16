package com.kmie.demo_booking.repository;

import com.kmie.demo_booking.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    List<Enquiry>
    findByEnquiryDateTimeGreaterThanEqualAndEnquiryDateTimeLessThan(
            LocalDateTime start,
            LocalDateTime end
    );
}