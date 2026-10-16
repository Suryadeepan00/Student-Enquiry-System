package com.kmie.demo_booking.service;

import com.kmie.demo_booking.dto.EnquiryRequest;
import com.kmie.demo_booking.entity.Enquiry;
import com.kmie.demo_booking.entity.Student;
import com.kmie.demo_booking.repository.EnquiryRepository;
import com.kmie.demo_booking.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EnquiryService {

    private final StudentRepository studentRepository;
    private final EnquiryRepository enquiryRepository;

    public EnquiryService(
            StudentRepository studentRepository,
            EnquiryRepository enquiryRepository
    ) {
        this.studentRepository = studentRepository;
        this.enquiryRepository = enquiryRepository;
    }

    @Transactional
    public Map<String, Object> createEnquiry(EnquiryRequest request) {

        Optional<Student> existingStudent =
                studentRepository.findByMobile(request.getMobile());

        boolean reusedStudent = existingStudent.isPresent();

        Student student;

        if (reusedStudent) {
            student = existingStudent.get();
        } else {
            student = new Student(
                    request.getName(),
                    request.getMobile(),
                    request.getEmail(),
                    request.getStudentClass()
            );

            student = studentRepository.save(student);
        }

        Enquiry enquiry = new Enquiry(
                student,
                request.getCourse(),
                request.getEnquirySource(),
                request.getEnquiryDateTime()
        );

        enquiry = enquiryRepository.save(enquiry);

        Map<String, Object> response = new LinkedHashMap<>();

        response.put(
                "message",
                reusedStudent
                        ? "Existing student found. New enquiry linked "
                        + "to the existing student."
                        : "Student registered and enquiry saved successfully."
        );

        response.put("existingStudent", reusedStudent);
        response.put("studentId", student.getId());
        response.put("studentName", student.getName());
        response.put("mobile", student.getMobile());
        response.put("enquiryId", enquiry.getId());
        response.put("course", enquiry.getCourse());
        response.put("enquirySource", enquiry.getEnquirySource());
        response.put("enquiryDateTime", enquiry.getEnquiryDateTime());

        return response;
    }
}