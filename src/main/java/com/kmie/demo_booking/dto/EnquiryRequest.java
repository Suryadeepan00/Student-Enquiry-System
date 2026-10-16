package com.kmie.demo_booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class EnquiryRequest {

    @NotBlank(message = "Student name is required")
    @Size(max = 100, message = "Student name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must contain exactly 10 digits"
    )
    private String mobile;

    @Email(message = "Enter a valid email address")
    @Size(max = 254, message = "Email must not exceed 254 characters")
    private String email;

    @NotBlank(message = "Student class is required")
    @Pattern(
            regexp = "^(11|12)$",
            message = "Student class must be 11 or 12"
    )
    private String studentClass;

    @NotBlank(message = "Course is required")
    @Pattern(
            regexp = "^(NEET Chemistry|JEE Chemistry)$",
            message = "Course must be NEET Chemistry or JEE Chemistry"
    )
    private String course;

    @NotBlank(message = "Enquiry source is required")
    @Size(max = 100, message = "Enquiry source must not exceed 100 characters")
    private String enquirySource;

    @NotNull(message = "Enquiry date and time are required")
    private LocalDateTime enquiryDateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null || email.isBlank()
                ? null
                : email.trim();
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass == null
                ? null
                : studentClass.trim();
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course == null ? null : course.trim();
    }

    public String getEnquirySource() {
        return enquirySource;
    }

    public void setEnquirySource(String enquirySource) {
        this.enquirySource = enquirySource == null
                ? null
                : enquirySource.trim();
    }

    public LocalDateTime getEnquiryDateTime() {
        return enquiryDateTime;
    }

    public void setEnquiryDateTime(LocalDateTime enquiryDateTime) {
        this.enquiryDateTime = enquiryDateTime;
    }
}