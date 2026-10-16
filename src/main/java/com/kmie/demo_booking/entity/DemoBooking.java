package com.kmie.demo_booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(
        name = "demo_bookings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_booking_student_date_slot",
                        columnNames = {
                                "student_id",
                                "demo_date",
                                "demo_slot"
                        }
                )
        }
)
public class DemoBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "demo_date", nullable = false)
    private LocalDate demoDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "demo_slot", nullable = false, length = 20)
    private DemoSlot demoSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.BOOKED;

    public DemoBooking() {
    }

    public DemoBooking(
            Student student,
            LocalDate demoDate,
            DemoSlot demoSlot
    ) {
        this.student = student;
        this.demoDate = demoDate;
        this.demoSlot = demoSlot;
        this.status = BookingStatus.BOOKED;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}