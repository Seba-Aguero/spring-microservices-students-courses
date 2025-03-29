package com.microservice.course.service;

import com.microservice.course.client.StudentClient;
import com.microservice.course.controller.dto.StudentDTO;
import com.microservice.course.entity.Course;
import com.microservice.course.exception.MicroserviceException;
import com.microservice.course.exception.ResourceNotFoundException;
import com.microservice.course.http.response.StudentByCouseResponse;
import com.microservice.course.persistence.ICourseRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private StudentClient studentClient;

    @Override
    public List<Course> findAll() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public StudentByCouseResponse findStudentsByCourseId(Long courseId) {

        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

            List<StudentDTO> studentDTOList;
            try {
                studentDTOList = studentClient.findAllStudentByCourse(course.getId());
            } catch (FeignException e) {
                log.error("Error communicating with student service: {}", e.getMessage());
                throw new MicroserviceException("Error retrieving students from student service", e);
            }

            return StudentByCouseResponse.builder()
                    .courseName(course.getName())
                    .teacher(course.getTeacher())
                    .studentDTOList(studentDTOList)
                    .build();
        } catch (ResourceNotFoundException e) {
            log.error("Unexpected error in findStudentsByCourseId: {}", e.getMessage());
            throw new MicroserviceException("Error processing request", e);
        }
    }
}

