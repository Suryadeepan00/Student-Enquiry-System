package com.kmie.demo_booking.service;

import com.kmie.demo_booking.entity.BookingStatus;
import com.kmie.demo_booking.entity.DemoBooking;
import com.kmie.demo_booking.entity.Enquiry;
import com.kmie.demo_booking.entity.Student;
import com.kmie.demo_booking.repository.DemoBookingRepository;
import com.kmie.demo_booking.repository.EnquiryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class CounsellorService {

    private final EnquiryRepository enquiryRepository;
    private final DemoBookingRepository bookingRepository;

    public CounsellorService(
            EnquiryRepository enquiryRepository,
            DemoBookingRepository bookingRepository
    ) {
        this.enquiryRepository = enquiryRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Map<String, Object>> getEnquiries(LocalDate date) {

        List<Enquiry> enquiries;

        if (date == null) {
            enquiries = enquiryRepository.findAll();
        } else {
            enquiries = enquiryRepository
                    .findByEnquiryDateTimeGreaterThanEqualAndEnquiryDateTimeLessThan(
                            date.atStartOfDay(),
                            date.plusDays(1).atStartOfDay()
                    );
        }

        return enquiries.stream()
                .map(this::enquiryResponse)
                .toList();
    }

    public List<Map<String, Object>> getBookings(
            LocalDate date,
            BookingStatus status
    ) {
        List<DemoBooking> bookings;

        if (date != null && status != null) {
            bookings = bookingRepository.findByDemoDateAndStatus(date, status);
        } else if (date != null) {
            bookings = bookingRepository.findByDemoDate(date);
        } else if (status != null) {
            bookings = bookingRepository.findByStatus(status);
        } else {
            bookings = bookingRepository.findAll();
        }

        return bookings.stream()
                .map(this::bookingResponse)
                .toList();
    }

    public Map<String, Long> getDashboard() {

        Map<String, Long> response = new LinkedHashMap<>();

        response.put("totalEnquiries", enquiryRepository.count());
        response.put("totalDemosBooked", bookingRepository.count());
        response.put(
                "currentlyBooked",
                bookingRepository.countByStatus(BookingStatus.BOOKED)
        );
        response.put(
                "totalDemosAttended",
                bookingRepository.countByStatus(BookingStatus.ATTENDED)
        );
        response.put(
                "totalDemosCancelled",
                bookingRepository.countByStatus(BookingStatus.CANCELLED)
        );

        return response;
    }

    private Map<String, Object> enquiryResponse(Enquiry enquiry) {

        Map<String, Object> response = studentDetails(enquiry.getStudent());

        response.put("enquiryId", enquiry.getId());
        response.put("course", enquiry.getCourse());
        response.put("enquirySource", enquiry.getEnquirySource());
        response.put("enquiryDateTime", enquiry.getEnquiryDateTime());

        return response;
    }

    private Map<String, Object> bookingResponse(DemoBooking booking) {

        Map<String, Object> response = studentDetails(booking.getStudent());

        response.put("bookingId", booking.getId());
        response.put("demoDate", booking.getDemoDate());
        response.put("demoSlot", booking.getDemoSlot());
        response.put("slotLabel", booking.getDemoSlot().getDisplayName());
        response.put("status", booking.getStatus());

        return response;
    }

    private Map<String, Object> studentDetails(Student student) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("studentId", student.getId());
        response.put("studentName", student.getName());
        response.put("mobile", student.getMobile());
        response.put("email", student.getEmail());
        response.put("studentClass", student.getStudentClass());

        return response;
    }
}