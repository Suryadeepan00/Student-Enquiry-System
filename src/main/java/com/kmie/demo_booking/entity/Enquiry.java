package com.kmie.demo_booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "enquiries")
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false, length = 50)
    private String course;

    @Column(name = "enquiry_source", nullable = false, length = 100)
    private String enquirySource;

    @Column(name = "enquiry_date_time", nullable = false)
    private LocalDateTime enquiryDateTime;

    public Enquiry() {
    }

    public Enquiry(
            Student student,
            String course,
            String enquirySource,
            LocalDateTime enquiryDateTime
    ) {
        this.student = student;
        this.course = course;
        this.enquirySource = enquirySource;
        this.enquiryDateTime = enquiryDateTime;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEnquirySource() {
        return enquirySource;
    }

    public void setEnquirySource(String enquirySource) {
        this.enquirySource = enquirySource;
    }

    public LocalDateTime getEnquiryDateTime() {
        return enquiryDateTime;
    }

    public void setEnquiryDateTime(LocalDateTime enquiryDateTime) {
        this.enquiryDateTime = enquiryDateTime;
    }
}