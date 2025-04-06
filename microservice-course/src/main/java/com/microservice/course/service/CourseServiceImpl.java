package com.microservice.course.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.course.client.StudentClient;
import com.microservice.course.controller.dto.StudentDTO;
import com.microservice.course.entity.Course;
import com.microservice.course.exception.MicroserviceException;
import com.microservice.course.exception.ResourceNotFoundException;
import com.microservice.course.http.response.StudentByCouseResponse;
import com.microservice.course.persistence.ICourseRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final ICourseRepository courseRepository;
    private final StudentClient studentClient;
    private final StudentCacheService studentCacheService;

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
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        List<StudentDTO> studentDTOList;

        // First try to get students from cache
        studentDTOList = studentCacheService.getStudentsByCourse(courseId);

        // If cache is empty, fetch from student service
        if (studentDTOList.isEmpty()) {
            log.info("Cache miss for course {}, fetching from student service", courseId);
            try {
                studentDTOList = studentClient.findAllStudentByCourse(courseId);
                // Update cache with fetched data
                for (StudentDTO student : studentDTOList) {
                    studentCacheService.addOrUpdateStudent(student, courseId);
                }
            } catch (FeignException e) {
                log.error("Error communicating with student service: {}", e.getMessage());
                throw new MicroserviceException("Error retrieving students from student service", e);
            }
        } else {
            log.info("Cache hit for course {}, using cached student data", courseId);
        }

        return StudentByCouseResponse.builder()
                .courseName(course.getName())
                .teacher(course.getTeacher())
                .studentDTOList(studentDTOList)
                .build();
    }
}

