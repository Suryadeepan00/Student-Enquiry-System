package com.kmie.demo_booking.repository;

import com.kmie.demo_booking.entity.BookingStatus;
import com.kmie.demo_booking.entity.DemoBooking;
import com.kmie.demo_booking.entity.DemoSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DemoBookingRepository
        extends JpaRepository<DemoBooking, Long> {

    boolean existsByStudent_IdAndDemoDateAndDemoSlot(
            Long studentId,
            LocalDate demoDate,
            DemoSlot demoSlot
    );

    List<DemoBooking> findByDemoDate(LocalDate demoDate);

    List<DemoBooking> findByStatus(BookingStatus status);

    List<DemoBooking> findByDemoDateAndStatus(
            LocalDate demoDate,
            BookingStatus status
    );

    long countByStatus(BookingStatus status);
}