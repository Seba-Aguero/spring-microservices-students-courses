package com.microservice.course.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservice.course.controller.dto.StudentDTO;

import java.util.List;

@FeignClient(name = "msvc-student", url = "student-service:8090")
public interface StudentClient {

    @GetMapping("/api/student/search-by-course/{courseId}")
    List<StudentDTO> findAllStudentByCourse(@PathVariable Long courseId);
}


