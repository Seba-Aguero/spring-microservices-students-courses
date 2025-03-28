package com.microservice.course.controller;

import com.microservice.course.entity.Course;
import com.microservice.course.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
@Tag(name = "Course Controller", description = "API for course management")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @Operation(summary = "Create a new course", description = "Creates a new course in the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid course data provided")
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCourse(@RequestBody Course course){
        courseService.save(course);
    }

    @Operation(summary = "Get all courses", description = "Returns a list of all courses")
    @ApiResponse(responseCode = "200", description = "List of courses retrieved successfully",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Course.class)))
    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok(courseService.findAll());
    }

    @Operation(summary = "Find course by ID", description = "Returns a course based on ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course found"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/search/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return ResponseEntity.ok(courseService.findById(id));
    }

    @Operation(summary = "Find students by course ID", description = "Returns all students enrolled in a specific course")
    @ApiResponse(responseCode = "200", description = "List of students for the course retrieved successfully")
    @GetMapping("/search-student/{courseId}")
    public ResponseEntity<?> findStudentsByCourseId(@PathVariable Long courseId){
        return ResponseEntity.ok(courseService.findStudentsByCourseId(courseId));
    }
}

