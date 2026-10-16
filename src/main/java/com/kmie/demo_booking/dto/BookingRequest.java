package com.kmie.demo_booking.dto;

import com.kmie.demo_booking.entity.DemoSlot;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class BookingRequest {

    @NotNull(message = "Student ID is required")
    @Positive(message = "Student ID must be greater than zero")
    private Long studentId;

    @NotNull(message = "Demo date is required")
    @FutureOrPresent(message = "Demo date cannot be in the past")
    private LocalDate demoDate;

    @NotNull(message = "Demo slot is required")
    private DemoSlot demoSlot;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getDemoDate() {
        return demoDate;
    }

    public void setDemoDate(LocalDate demoDate) {
        this.demoDate = demoDate;
    }

    public DemoSlot getDemoSlot() {
        return demoSlot;
    }

    public void setDemoSlot(DemoSlot demoSlot) {
        this.demoSlot = demoSlot;
    }
}