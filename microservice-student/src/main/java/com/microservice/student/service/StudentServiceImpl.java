package com.microservice.student.service;

import com.microservice.student.controller.dto.StudentUpdateDTO;
import com.microservice.student.entity.Student;
import com.microservice.student.messaging.service.StudentEventPublisher;
import com.microservice.student.persistence.IStudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private final IStudentRepository studentRepository;
    private final StudentEventPublisher studentEventPublisher;

    @Override
    public List<Student> findAll() {
        return (List<Student>) studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    @Override
    public void save(Student student) {
        if (student.getCourseId() != null) {
            if (student.getCourseId() <= 0) {
                throw new IllegalArgumentException("Course ID must be a positive number");
            }
        }

        List<Student> existingStudents = studentRepository.findByEmail(student.getEmail());
        if (!existingStudents.isEmpty()) {
            if (student.getId() == null || !existingStudents.get(0).getId().equals(student.getId())) {
                throw new IllegalArgumentException("Email already exists: " + student.getEmail());
            }
        }

        Student savedStudent = studentRepository.save(student);

        try {
            studentEventPublisher.publishStudentCreated(savedStudent);
        } catch (Exception e) {
            log.error("Error publishing student created event: {}", e.getMessage());
        }
    }

    @Override
    public List<Student> findAllByCourseId(Long courseId) {
        return studentRepository.findAllByCourseId(courseId);
    }

    @Override
    public void delete(Long id) {
        Student student = findById(id);
        studentRepository.delete(student);
        studentEventPublisher.publishStudentDeleted(student);
    }

    @Override
    public Student update(Long id, StudentUpdateDTO studentDTO) {
        Student existingStudent = findById(id);

        existingStudent.setName(studentDTO.getName());
        existingStudent.setLastName(studentDTO.getLastName());
        existingStudent.setEmail(studentDTO.getEmail());
        existingStudent.setCourseId(studentDTO.getCourseId());

        // Validate email uniqueness
        List<Student> studentsWithSameEmail = studentRepository.findByEmail(studentDTO.getEmail());
        if (!studentsWithSameEmail.isEmpty() &&
                !studentsWithSameEmail.get(0).getId().equals(id)) {
            throw new IllegalArgumentException("Email already exists: " + studentDTO.getEmail());
        }

        if (studentDTO.getCourseId() != null && studentDTO.getCourseId() <= 0) {
            throw new IllegalArgumentException("Course ID must be a positive number");
        }

        studentEventPublisher.publishStudentUpdated(existingStudent);

        return studentRepository.save(existingStudent);
    }
}

