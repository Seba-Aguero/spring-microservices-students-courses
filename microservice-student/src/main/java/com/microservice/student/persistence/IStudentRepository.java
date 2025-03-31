package com.microservice.student.persistence;

import com.microservice.student.entity.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStudentRepository extends CrudRepository<Student, Long> {
    List<Student> findAllByCourseId(Long courseId);
    List<Student> findByEmail(String email);
}

