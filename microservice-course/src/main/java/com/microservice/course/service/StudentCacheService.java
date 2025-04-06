package com.microservice.course.service;

import com.microservice.course.controller.dto.StudentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class StudentCacheService {

    // Map to store students by course
    private final Map<Long, Map<Long, StudentDTO>> studentsByCourse = new ConcurrentHashMap<>();

    public void addOrUpdateStudent(StudentDTO student, Long courseId) {
        if (courseId == null) {
            return;
        }

        studentsByCourse.computeIfAbsent(courseId, k -> new ConcurrentHashMap<>())
                .put(student.getId(), student);

        log.info("Student with ID {} added/updated in cache for course {}", student.getId(), courseId);
    }

    public void removeStudent(Long studentId, Long courseId) {
        if (courseId == null || !studentsByCourse.containsKey(courseId)) {
            return;
        }

        studentsByCourse.get(courseId).remove(studentId);
        log.info("Student with ID {} removed from cache for course {}", studentId, courseId);
    }

    public List<StudentDTO> getStudentsByCourse(Long courseId) {
        if (!studentsByCourse.containsKey(courseId)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(studentsByCourse.get(courseId).values());
    }

    public void updateStudentCourse(Long studentId, Long oldCourseId, Long newCourseId) {
        if (oldCourseId != null && studentsByCourse.containsKey(oldCourseId)) {
            StudentDTO student = studentsByCourse.get(oldCourseId).remove(studentId);
            if (student != null && newCourseId != null) {
                student.setCourseId(newCourseId);
                addOrUpdateStudent(student, newCourseId);
                log.info("Student with ID {} moved from course {} to course {}",
                        studentId, oldCourseId, newCourseId);
            }
        }
    }
}
