package com.microservice.student.service;

import com.microservice.student.controller.dto.StudentUpdateDTO;
import com.microservice.student.entity.Student;

import java.util.List;

public interface IStudentService {
    List<Student> findAll();

    Student findById(Long id);

    void save(Student student);

    Student update(Long id, StudentUpdateDTO studentDTO);

    void delete(Long id);

    List<Student> findAllByCourseId(Long courseId);
}

