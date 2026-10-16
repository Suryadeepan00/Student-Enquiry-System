package com.kmie.demo_booking.repository;

import com.kmie.demo_booking.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByMobile(String mobile);
}