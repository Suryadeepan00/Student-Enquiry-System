package com.kmie.demo_booking;

import com.kmie.demo_booking.entity.BookingStatus;
import com.kmie.demo_booking.entity.DemoBooking;
import com.kmie.demo_booking.entity.DemoSlot;
import com.kmie.demo_booking.entity.Student;
import com.kmie.demo_booking.repository.DemoBookingRepository;
import com.kmie.demo_booking.repository.EnquiryRepository;
import com.kmie.demo_booking.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:kmie-tests;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class KmieWorkflowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private DemoBookingRepository bookingRepository;

    @BeforeEach
    void clearTestDatabase() {
        bookingRepository.deleteAll();
        enquiryRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void validEnquiryCreatesStudentAndEnquiry() throws Exception {
        mockMvc.perform(post("/api/enquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enquiryJson("9876543210", "12")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.existingStudent").value(false))
                .andExpect(jsonPath("$.studentId").isNumber())
                .andExpect(jsonPath("$.enquiryId").isNumber());

        assertEquals(1L, studentRepository.count());
        assertEquals(1L, enquiryRepository.count());
    }

    @Test
    void duplicateMobileReusesStudentAndCreatesAnotherEnquiry()
            throws Exception {

        String request = enquiryJson("9876543210", "12");

        mockMvc.perform(post("/api/enquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        Long studentId = studentRepository
                .findByMobile("9876543210")
                .orElseThrow()
                .getId();

        mockMvc.perform(post("/api/enquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.existingStudent").value(true))
                .andExpect(jsonPath("$.studentId")
                        .value(studentId.intValue()));

        assertEquals(1L, studentRepository.count());
        assertEquals(2L, enquiryRepository.count());
    }

    @Test
    void invalidMobileReturns400WithoutSavingRecords() throws Exception {
        mockMvc.perform(post("/api/enquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enquiryJson("1234", "12")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.mobile").exists());

        assertEquals(0L, studentRepository.count());
        assertEquals(0L, enquiryRepository.count());
    }

    @Test
    void invalidClassReturns400() throws Exception {
        mockMvc.perform(post("/api/enquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enquiryJson("9876543210", "10")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.studentClass").exists());

        assertEquals(0L, studentRepository.count());
    }

    @Test
    void validBookingReturnsConfirmation() throws Exception {
        Student student = createStudent();
        LocalDate date = LocalDate.now().plusDays(1);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson(
                                student.getId(), date, "SLOT_3_PM"
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("BOOKED"))
                .andExpect(jsonPath("$.slotLabel").value("3 PM"))
                .andExpect(jsonPath("$.confirmationMessage").isString());

        assertEquals(1L, bookingRepository.count());
    }

    @Test
    void duplicateBookingReturns409() throws Exception {
        Student student = createStudent();
        LocalDate date = LocalDate.now().plusDays(1);
        String request = bookingJson(
                student.getId(), date, "SLOT_3_PM"
        );

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict());

        assertEquals(1L, bookingRepository.count());
    }

    @Test
    void missingStudentReturns404() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson(
                                999999L,
                                LocalDate.now().plusDays(1),
                                "SLOT_3_PM"
                        )))
                .andExpect(status().isNotFound());

        assertEquals(0L, bookingRepository.count());
    }

    @Test
    void pastDemoDateReturns400() throws Exception {
        Student student = createStudent();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson(
                                student.getId(),
                                LocalDate.now().minusDays(1),
                                "SLOT_3_PM"
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.demoDate").exists());

        assertEquals(0L, bookingRepository.count());
    }

    @Test
    void invalidSlotReturns400() throws Exception {
        Student student = createStudent();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson(
                                student.getId(),
                                LocalDate.now().plusDays(1),
                                "SLOT_2_PM"
                        )))
                .andExpect(status().isBadRequest());

        assertEquals(0L, bookingRepository.count());
    }

    @Test
    void statusUpdateChangesSavedBooking() throws Exception {
        DemoBooking booking = createBooking();

        mockMvc.perform(patch(
                        "/api/bookings/{id}/status",
                        booking.getId()
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "ATTENDED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATTENDED"));

        assertEquals(
                BookingStatus.ATTENDED,
                bookingRepository.findById(booking.getId())
                        .orElseThrow()
                        .getStatus()
        );
    }

    @Test
    void bookingDateAndStatusFiltersReturnMatchingRecord()
            throws Exception {

        DemoBooking booking = createBooking();

        mockMvc.perform(get("/api/bookings")
                        .param("date", booking.getDemoDate().toString())
                        .param("status", "BOOKED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/api/bookings")
                        .param("date", booking.getDemoDate().toString())
                        .param("status", "ATTENDED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void dashboardShowsCorrectBookingCounts() throws Exception {
        DemoBooking booking = createBooking();
        booking.setStatus(BookingStatus.ATTENDED);
        bookingRepository.save(booking);

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEnquiries").value(0))
                .andExpect(jsonPath("$.totalDemosBooked").value(1))
                .andExpect(jsonPath("$.currentlyBooked").value(0))
                .andExpect(jsonPath("$.totalDemosAttended").value(1))
                .andExpect(jsonPath("$.totalDemosCancelled").value(0));
    }

    private Student createStudent() {
        return studentRepository.save(new Student(
                "Test Student",
                "9876543210",
                null,
                "12"
        ));
    }

    private DemoBooking createBooking() {
        return bookingRepository.save(new DemoBooking(
                createStudent(),
                LocalDate.now().plusDays(1),
                DemoSlot.SLOT_3_PM
        ));
    }

    private String enquiryJson(String mobile, String studentClass) {
        return """
                {
                  "name": "Test Student",
                  "mobile": "%s",
                  "email": "",
                  "studentClass": "%s",
                  "course": "NEET Chemistry",
                  "enquirySource": "Website",
                  "enquiryDateTime": "2026-10-14T10:00:00"
                }
                """.formatted(mobile, studentClass);
    }

    private String bookingJson(
            Long studentId,
            LocalDate date,
            String slot
    ) {
        return """
                {
                  "studentId": %d,
                  "demoDate": "%s",
                  "demoSlot": "%s"
                }
                """.formatted(studentId, date, slot);
    }
}