package com.microservice.student.service;

import java.util.List;

import com.microservice.student.entity.Student;

public interface IStudentService {

    List<Student> findAll();
    Student findById(Long id);
    void save(Student student);
    List<Student> findByCourseId(Long courseId);
}
