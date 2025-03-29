package com.microservice.student.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.microservice.student.entity.Student;

import java.util.List;

@Repository
public interface IStudentRepository extends CrudRepository<Student, Long> {

    List<Student> findAllByCourseId(Long courseId);
}
