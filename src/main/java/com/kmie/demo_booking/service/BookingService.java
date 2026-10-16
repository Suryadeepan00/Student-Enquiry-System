package com.kmie.demo_booking.service;

import com.kmie.demo_booking.dto.BookingRequest;
import com.kmie.demo_booking.dto.StatusUpdateRequest;
import com.kmie.demo_booking.entity.DemoBooking;
import com.kmie.demo_booking.entity.Student;
import com.kmie.demo_booking.exception.DuplicateBookingException;
import com.kmie.demo_booking.exception.ResourceNotFoundException;
import com.kmie.demo_booking.repository.DemoBookingRepository;
import com.kmie.demo_booking.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class BookingService {

    private final StudentRepository studentRepository;
    private final DemoBookingRepository bookingRepository;

    public BookingService(
            StudentRepository studentRepository,
            DemoBookingRepository bookingRepository
    ) {
        this.studentRepository = studentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Map<String, Object> createBooking(BookingRequest request) {

        Student student = studentRepository
                .findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with ID: " + request.getStudentId()
                ));

        boolean duplicate = bookingRepository
                .existsByStudent_IdAndDemoDateAndDemoSlot(
                        student.getId(),
                        request.getDemoDate(),
                        request.getDemoSlot()
                );

        if (duplicate) {
            throw new DuplicateBookingException(
                    "This student already has a booking for the selected "
                            + "date and slot."
            );
        }

        DemoBooking booking = new DemoBooking(
                student,
                request.getDemoDate(),
                request.getDemoSlot()
        );

        booking = bookingRepository.save(booking);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd MMMM yyyy",
                Locale.ENGLISH
        );

        String confirmationMessage =
                "Hello " + student.getName()
                        + ", your demo at Komali Mam Institute of Excellence "
                        + "(KMIE) is booked for "
                        + booking.getDemoDate().format(formatter)
                        + " at " + booking.getDemoSlot().getDisplayName()
                        + ". Your booking ID is " + booking.getId() + ".";

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("message", "Demo booked successfully.");
        response.put("bookingId", booking.getId());
        response.put("studentId", student.getId());
        response.put("studentName", student.getName());
        response.put("demoDate", booking.getDemoDate());
        response.put("demoSlot", booking.getDemoSlot());
        response.put("slotLabel", booking.getDemoSlot().getDisplayName());
        response.put("status", booking.getStatus());
        response.put("confirmationMessage", confirmationMessage);

        return response;
    }

    @Transactional
    public Map<String, Object> updateStatus(
            Long bookingId,
            StatusUpdateRequest request
    ) {
        if (bookingId == null || bookingId <= 0) {
            throw new IllegalArgumentException(
                    "Booking ID must be greater than zero."
            );
        }

        DemoBooking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with ID: " + bookingId
                ));

        booking.setStatus(request.getStatus());

        booking = bookingRepository.save(booking);

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("message", "Booking status updated successfully.");
        response.put("bookingId", booking.getId());
        response.put("studentId", booking.getStudent().getId());
        response.put("demoDate", booking.getDemoDate());
        response.put("demoSlot", booking.getDemoSlot());
        response.put("status", booking.getStatus());

        return response;
    }
}